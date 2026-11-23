/**
 * 3D Bin Packing Heuristic Solver
 * Pallet volume utilization & spatial load balancing engine
 * 
 * Supports:
 * - 6-degree orthogonal rotation evaluation
 * - Extreme Points (EP) 3D placement heuristic with gravity support validation
 * - Dynamic Center-of-Gravity (CoG) & physical stability scoring
 * - Multi-pallet automatic overflow packing
 * - Dual ES Module & CommonJS compatibility
 */

export class BinPacker3D {
  /**
   * @param {Object} options
   * @param {Object} [options.palletDimensions] - Standard container/pallet specification
   * @param {number} [options.palletDimensions.width=120] - Pallet width in cm (Euro-pallet default 120cm)
   * @param {number} [options.palletDimensions.depth=80] - Pallet depth in cm (Euro-pallet default 80cm)
   * @param {number} [options.palletDimensions.height=160] - Pallet usable height in cm (default 160cm)
   * @param {number} [options.palletDimensions.maxWeightKg=1000] - Max load capacity in kg (default 1000kg)
   * @param {boolean} [options.allowRotation=true] - Whether 3D rotations are permitted
   * @param {boolean} [options.enforceSupport=true] - Whether elevated items require base support
   * @param {number} [options.minSupportRatio=0.6] - Minimum base area support required (0.0 - 1.0)
   */
  constructor(options = {}) {
    this.palletSpec = {
      width: options.palletDimensions?.width ?? 120,
      depth: options.palletDimensions?.depth ?? 80,
      height: options.palletDimensions?.height ?? 160,
      maxWeightKg: options.palletDimensions?.maxWeightKg ?? 1000,
    };
    this.allowRotation = options.allowRotation ?? true;
    this.enforceSupport = options.enforceSupport ?? true;
    this.minSupportRatio = options.minSupportRatio ?? 0.6;
  }

  /**
   * Pack a collection of inventory boxes into one or more pallets
   * @param {Array<Object>} items - Array of box definitions { id, sku, width, depth, height, weight, quantity, allowRotation }
   * @param {Object} [overrideSpec] - Optional custom pallet override
   * @returns {Object} Comprehensive packing plan with pallet allocations, coordinates, and metrics
   */
  pack(items, overrideSpec = null) {
    const spec = overrideSpec ? { ...this.palletSpec, ...overrideSpec } : this.palletSpec;
    const palletVolume = spec.width * spec.depth * spec.height;

    // Expand items by quantity and generate flat item list
    const unplacedItems = [];
    items.forEach((item, itemIdx) => {
      const qty = item.quantity && item.quantity > 0 ? item.quantity : 1;
      for (let q = 0; q < qty; q++) {
        unplacedItems.push({
          id: item.id ? `${item.id}-${q + 1}` : `BOX-${itemIdx + 1}-${q + 1}`,
          sku: item.sku || item.id || `SKU-${itemIdx + 1}`,
          width: Number(item.width),
          depth: Number(item.depth),
          height: Number(item.height),
          weight: Number(item.weight || 1),
          volume: Number(item.width) * Number(item.depth) * Number(item.height),
          allowRotation: item.allowRotation !== undefined ? item.allowRotation : this.allowRotation,
          name: item.name || `Carton ${item.sku || itemIdx + 1}`
        });
      }
    });

    // Sort items by Volume Descending, then Weight Descending (Best Fit Decreasing)
    unplacedItems.sort((a, b) => {
      if (b.volume !== a.volume) return b.volume - a.volume;
      return b.weight - a.weight;
    });

    const pallets = [];
    let remaining = [...unplacedItems];
    let palletId = 1;

    while (remaining.length > 0) {
      const palletResult = this._packSinglePallet(remaining, spec, palletId);
      
      // If nothing could be packed in an empty pallet, the remaining items cannot fit the pallet dimensions
      if (palletResult.packedBoxes.length === 0) {
        break;
      }

      pallets.push(palletResult);
      remaining = palletResult.unpackedItems;
      palletId++;
    }

    // Calculate aggregated fleet summary
    const totalPacked = pallets.reduce((sum, p) => sum + p.packedBoxes.length, 0);
    const avgVolumeUtil = pallets.length > 0
      ? pallets.reduce((sum, p) => sum + p.volumeUtilizationPct, 0) / pallets.length
      : 0;

    return {
      success: remaining.length === 0,
      palletDimensions: spec,
      totalItemsRequested: unplacedItems.length,
      totalItemsPacked: totalPacked,
      totalItemsUnpacked: remaining.length,
      palletsUsedCount: pallets.length,
      averageVolumeUtilizationPct: Number(avgVolumeUtil.toFixed(2)),
      pallets,
      unpackedItems: remaining
    };
  }

  /**
   * Internal routine to pack a single pallet using Extreme Points 3D heuristic
   * @private
   */
  _packSinglePallet(items, spec, palletNumber) {
    const palletVolume = spec.width * spec.depth * spec.height;
    const packedBoxes = [];
    const unpackedItems = [];
    let currentWeight = 0;

    // Extreme points pool: initialized with origin [0, 0, 0]
    // Coordinates: x (width), y (height / vertical), z (depth)
    let extremePoints = [{ x: 0, y: 0, z: 0 }];

    for (const box of items) {
      // Check weight capacity threshold
      if (currentWeight + box.weight > spec.maxWeightKg) {
        unpackedItems.push(box);
        continue;
      }

      const orientations = this._getOrientations(box);
      let bestPlacement = null;

      // Sort candidate points: Lowest Y (ground up), Lowest Z, Lowest X
      this._sortExtremePoints(extremePoints);

      for (const pt of extremePoints) {
        for (const ori of orientations) {
          if (this._fitsInPallet(pt, ori, spec)) {
            if (!this._collidesWithPacked(pt, ori, packedBoxes)) {
              if (!this.enforceSupport || this._hasAdequateSupport(pt, ori, packedBoxes)) {
                // Score placement: prefer lower Y, then tightest bounding volume
                const score = pt.y * 10000 + pt.z * 100 + pt.x;
                if (!bestPlacement || score < bestPlacement.score) {
                  bestPlacement = { point: pt, orientation: ori, score };
                  break; // Greedy choice at lowest valid extreme point
                }
              }
            }
          }
        }
        if (bestPlacement) break;
      }

      if (bestPlacement) {
        const placedBox = {
          ...box,
          x: bestPlacement.point.x,
          y: bestPlacement.point.y,
          z: bestPlacement.point.z,
          width: bestPlacement.orientation.width,
          height: bestPlacement.orientation.height,
          depth: bestPlacement.orientation.depth,
          rotationIndex: bestPlacement.orientation.rotationIndex
        };

        packedBoxes.push(placedBox);
        currentWeight += box.weight;

        // Update extreme points
        extremePoints = this._generateNewExtremePoints(placedBox, packedBoxes, extremePoints, spec);
      } else {
        unpackedItems.push(box);
      }
    }

    // Compute metrics
    const totalPackedVolume = packedBoxes.reduce((acc, b) => acc + (b.width * b.height * b.depth), 0);
    const volumeUtilizationPct = palletVolume > 0 ? (totalPackedVolume / palletVolume) * 100 : 0;
    const weightUtilizationPct = spec.maxWeightKg > 0 ? (currentWeight / spec.maxWeightKg) * 100 : 0;
    const centerOfGravity = this._computeCenterOfGravity(packedBoxes, currentWeight);
    const stabilityScore = this._computeStabilityScore(centerOfGravity, spec);

    return {
      palletNumber,
      totalPackedBoxes: packedBoxes.length,
      volumeUtilizationPct: Number(volumeUtilizationPct.toFixed(2)),
      weightUtilizationPct: Number(weightUtilizationPct.toFixed(2)),
      totalWeightKg: Number(currentWeight.toFixed(2)),
      packedVolumeCm3: totalPackedVolume,
      palletCapacityCm3: palletVolume,
      centerOfGravity,
      stabilityScore: Number(stabilityScore.toFixed(2)),
      packedBoxes,
      unpackedItems
    };
  }

  /**
   * Generate 6 orthogonal 3D rotations or single orientation if restricted
   * @private
   */
  _getOrientations(box) {
    const { width: w, height: h, depth: d } = box;
    if (!box.allowRotation) {
      return [{ width: w, height: h, depth: d, rotationIndex: 0 }];
    }

    // 6 unique permutations of dimensions (w, h, d)
    const rawOrientations = [
      { width: w, height: h, depth: d, rotationIndex: 0 },
      { width: w, height: d, depth: h, rotationIndex: 1 },
      { width: h, height: w, depth: d, rotationIndex: 2 },
      { width: h, height: d, depth: w, rotationIndex: 3 },
      { width: d, height: w, depth: h, rotationIndex: 4 },
      { width: d, height: h, depth: w, rotationIndex: 5 },
    ];

    // Deduplicate identical geometric bounding boxes
    const unique = [];
    const seen = new Set();
    for (const ori of rawOrientations) {
      const key = `${ori.width}-${ori.height}-${ori.depth}`;
      if (!seen.has(key)) {
        seen.add(key);
        unique.push(ori);
      }
    }
    return unique;
  }

  /**
   * Check if box dimensions fit inside pallet bounds
   * @private
   */
  _fitsInPallet(pt, ori, spec) {
    return (
      pt.x + ori.width <= spec.width &&
      pt.y + ori.height <= spec.height &&
      pt.z + ori.depth <= spec.depth
    );
  }

  /**
   * Check 3D AABB bounding box collision with previously packed boxes
   * @private
   */
  _collidesWithPacked(pt, ori, packedBoxes) {
    const boxMinX = pt.x;
    const boxMaxX = pt.x + ori.width;
    const boxMinY = pt.y;
    const boxMaxY = pt.y + ori.height;
    const boxMinZ = pt.z;
    const boxMaxZ = pt.z + ori.depth;

    for (const b of packedBoxes) {
      const bMinX = b.x;
      const bMaxX = b.x + b.width;
      const bMinY = b.y;
      const bMaxY = b.y + b.height;
      const bMinZ = b.z;
      const bMaxZ = b.z + b.depth;

      // Overlap occurs if boundaries overlap across all three spatial axes
      const overlapX = boxMinX < bMaxX && boxMaxX > bMinX;
      const overlapY = boxMinY < bMaxY && boxMaxY > bMinY;
      const overlapZ = boxMinZ < bMaxZ && boxMaxZ > bMinZ;

      if (overlapX && overlapY && overlapZ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Validate physical support directly below the candidate box
   * @private
   */
  _hasAdequateSupport(pt, ori, packedBoxes) {
    // If sitting on the pallet floor (y = 0), 100% supported
    if (pt.y === 0) return true;

    const boxBaseArea = ori.width * ori.depth;
    let supportedArea = 0;

    const bMinX = pt.x;
    const bMaxX = pt.x + ori.width;
    const bMinZ = pt.z;
    const bMaxZ = pt.z + ori.depth;

    for (const b of packedBoxes) {
      // Check if box top aligns with candidate box bottom (y == b.y + b.height)
      if (Math.abs((b.y + b.height) - pt.y) < 1e-4) {
        const overlapMinX = Math.max(bMinX, b.x);
        const overlapMaxX = Math.min(bMaxX, b.x + b.width);
        const overlapMinZ = Math.max(bMinZ, b.z);
        const overlapMaxZ = Math.min(bMaxZ, b.z + b.depth);

        if (overlapMaxX > overlapMinX && overlapMaxZ > overlapMinZ) {
          supportedArea += (overlapMaxX - overlapMinX) * (overlapMaxZ - overlapMinZ);
        }
      }
    }

    return (supportedArea / boxBaseArea) >= this.minSupportRatio;
  }

  /**
   * Generate next 3D extreme points after placing a box
   * @private
   */
  _generateNewExtremePoints(placedBox, packedBoxes, currentPoints, spec) {
    const candidatePoints = [
      { x: placedBox.x + placedBox.width, y: placedBox.y, z: placedBox.z },
      { x: placedBox.x, y: placedBox.y + placedBox.height, z: placedBox.z },
      { x: placedBox.x, y: placedBox.y, z: placedBox.z + placedBox.depth }
    ];

    // Combine existing remaining points + new points
    const combined = [...currentPoints, ...candidatePoints];
    const validPoints = [];
    const seen = new Set();

    for (const pt of combined) {
      // Must be within pallet boundaries
      if (pt.x >= spec.width || pt.y >= spec.height || pt.z >= spec.depth) continue;

      // Must not be inside any packed box
      let insideAny = false;
      for (const b of packedBoxes) {
        if (
          pt.x >= b.x && pt.x < (b.x + b.width) &&
          pt.y >= b.y && pt.y < (b.y + b.height) &&
          pt.z >= b.z && pt.z < (b.z + b.depth)
        ) {
          insideAny = true;
          break;
        }
      }
      if (insideAny) continue;

      const key = `${pt.x},${pt.y},${pt.z}`;
      if (!seen.has(key)) {
        seen.add(key);
        validPoints.push(pt);
      }
    }

    return validPoints;
  }

  /**
   * Sort extreme points prioritizing gravity-stable ground-up packing
   * @private
   */
  _sortExtremePoints(points) {
    points.sort((a, b) => {
      if (a.y !== b.y) return a.y - b.y; // Layer by layer from bottom
      if (a.z !== b.z) return a.z - b.z; // Row by row
      return a.x - b.x;                  // Column by column
    });
  }

  /**
   * Compute 3D mass-weighted Center of Gravity (CoG)
   * @private
   */
  _computeCenterOfGravity(packedBoxes, totalWeight) {
    if (packedBoxes.length === 0 || totalWeight === 0) {
      return { x: 0, y: 0, z: 0 };
    }

    let sumX = 0;
    let sumY = 0;
    let sumZ = 0;

    for (const b of packedBoxes) {
      const centerX = b.x + (b.width / 2);
      const centerY = b.y + (b.height / 2);
      const centerZ = b.z + (b.depth / 2);

      sumX += centerX * b.weight;
      sumY += centerY * b.weight;
      sumZ += centerZ * b.weight;
    }

    return {
      x: Number((sumX / totalWeight).toFixed(2)),
      y: Number((sumY / totalWeight).toFixed(2)),
      z: Number((sumZ / totalWeight).toFixed(2))
    };
  }

  /**
   * Calculate stability score (0-100) based on deviation of CoG from pallet geometric midpoint
   * @private
   */
  _computeStabilityScore(cog, spec) {
    const idealX = spec.width / 2;
    const idealZ = spec.depth / 2;

    const devX = Math.abs(cog.x - idealX) / idealX;
    const devZ = Math.abs(cog.z - idealZ) / idealZ;
    const heightFactor = Math.min(1.0, cog.y / (spec.height * 0.7)); // lower CoG is more stable

    // Base stability 100 minus lateral deviations & elevation penalty
    const lateralPenalty = (devX * 35) + (devZ * 35);
    const heightPenalty = heightFactor * 30;

    const score = Math.max(0, Math.min(100, 100 - lateralPenalty - heightPenalty));
    return score;
  }
}

// Support dual export formats
export default BinPacker3D;
if (typeof module !== 'undefined' && module.exports) {
  module.exports = BinPacker3D;
  module.exports.default = BinPacker3D;
  module.exports.BinPacker3D = BinPacker3D;
}
