import React, { useState, useMemo } from 'react';

/**
 * Interactive 2D Warehouse Grid Heatmap
 * Visualizes order picker traffic density, aisle congestion bottlenecks, and SKU rack bay activity.
 */

export function WarehouseHeatmap({
  aisles = ['Aisle-A', 'Aisle-B', 'Aisle-C', 'Aisle-D', 'Aisle-E', 'Aisle-F'],
  baysCount = 8,
  initialTrafficData = null
}) {
  const [selectedCell, setSelectedCell] = useState(null);
  const [activeMetric, setActiveMetric] = useState('congestion'); // 'congestion' | 'picksPerHour' | 'dwellSeconds'

  // Generate or memoize grid density values
  const gridData = useMemo(() => {
    if (initialTrafficData) return initialTrafficData;

    const data = {};
    aisles.forEach((aisle, aIdx) => {
      data[aisle] = [];
      for (let b = 1; b <= baysCount; b++) {
        // Seed realistic warehouse congestion hotspots near center aisles
        const isCenter = aIdx >= 2 && aIdx <= 4 && b >= 3 && b <= 6;
        const baseCongestion = isCenter ? 75 : 25;
        const congestionPct = Math.min(100, Math.max(5, baseCongestion + Math.floor(Math.sin(aIdx + b) * 20)));

        data[aisle].push({
          aisle,
          bay: b,
          bayCode: `${aisle}-B${b < 10 ? '0' + b : b}`,
          congestionPct,
          picksPerHour: Math.round(congestionPct * 1.8),
          dwellSeconds: Math.round(15 + (congestionPct * 0.8)),
          activePickers: congestionPct > 80 ? 4 : (congestionPct > 50 ? 2 : 1),
          topSkus: [`SKU-${aIdx + 10}${b}`, `SKU-${aIdx + 20}${b}`]
        });
      }
    });
    return data;
  }, [aisles, baysCount, initialTrafficData]);

  // Color mapping based on congestion density
  const getCellColor = (val) => {
    if (val >= 80) return 'bg-rose-500 text-white hover:bg-rose-600';
    if (val >= 60) return 'bg-amber-400 text-slate-900 hover:bg-amber-500';
    if (val >= 35) return 'bg-emerald-400 text-slate-900 hover:bg-emerald-500';
    return 'bg-slate-100 text-slate-600 hover:bg-slate-200';
  };

  // Find worst bottleneck bay
  const allBays = Object.values(gridData).flat();
  const maxBottleneck = [...allBays].sort((a, b) => b.congestionPct - a.congestionPct)[0];

  return (
    <div className="w-full bg-white rounded-xl border border-slate-200 shadow-sm p-6 font-sans">
      {/* Header & Controls */}
      <div className="flex flex-col md:flex-row md:items-center justify-between pb-4 border-b border-slate-100 mb-6 gap-4">
        <div>
          <h2 className="text-lg font-bold text-slate-800 flex items-center gap-2">
            <span>🏭</span> Warehouse Picker Congestion Heatmap
          </h2>
          <p className="text-xs text-slate-500">Real-time aisle density and picker routing congestion analysis</p>
        </div>

        <div className="flex items-center gap-3">
          <div className="text-xs bg-slate-100 rounded-lg p-1 flex">
            <button
              onClick={() => setActiveMetric('congestion')}
              className={`px-3 py-1 rounded-md font-medium transition ${activeMetric === 'congestion' ? 'bg-white shadow text-slate-800' : 'text-slate-500'}`}
            >
              Congestion %
            </button>
            <button
              onClick={() => setActiveMetric('picksPerHour')}
              className={`px-3 py-1 rounded-md font-medium transition ${activeMetric === 'picksPerHour' ? 'bg-white shadow text-slate-800' : 'text-slate-500'}`}
            >
              Picks / Hr
            </button>
          </div>

          {maxBottleneck && maxBottleneck.congestionPct >= 80 && (
            <div className="px-3 py-1 bg-rose-50 border border-rose-200 text-rose-700 text-xs rounded-full font-semibold flex items-center gap-1.5 animate-pulse">
              <span className="w-2 h-2 rounded-full bg-rose-500"></span>
              Bottleneck: {maxBottleneck.bayCode} ({maxBottleneck.congestionPct}%)
            </div>
          )}
        </div>
      </div>

      {/* Grid Canvas */}
      <div className="overflow-x-auto">
        <div className="inline-block min-w-full">
          {/* Bay column headers */}
          <div className="flex items-center mb-2 pl-24">
            {Array.from({ length: baysCount }, (_, i) => (
              <div key={i} className="w-14 text-center text-xs font-semibold text-slate-400">
                Bay {i + 1}
              </div>
            ))}
          </div>

          {/* Aisle Rows */}
          <div className="space-y-2">
            {aisles.map(aisle => (
              <div key={aisle} className="flex items-center">
                <span className="w-24 text-xs font-bold text-slate-600 truncate pr-2 text-right">
                  {aisle}
                </span>

                <div className="flex gap-2">
                  {gridData[aisle].map(cell => {
                    const displayVal = activeMetric === 'congestion'
                      ? `${cell.congestionPct}%`
                      : cell.picksPerHour;

                    const isSelected = selectedCell?.bayCode === cell.bayCode;

                    return (
                      <button
                        key={cell.bayCode}
                        onClick={() => setSelectedCell(cell)}
                        className={`w-14 h-12 rounded-lg text-xs font-semibold flex flex-col items-center justify-center transition cursor-pointer ${getCellColor(cell.congestionPct)} ${
                          isSelected ? 'ring-4 ring-indigo-500 scale-105 z-10' : ''
                        }`}
                        title={`${cell.bayCode}: ${cell.congestionPct}% congestion`}
                      >
                        <span>{displayVal}</span>
                        <span className="text-[9px] opacity-75">{cell.activePickers}👤</span>
                      </button>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Legend & Selected Bay Inspector */}
      <div className="mt-6 pt-4 border-t border-slate-100 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 text-xs">
        {/* Legend */}
        <div className="flex items-center gap-4">
          <span className="text-slate-500 font-medium">Density:</span>
          <div className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-slate-100 border"></span> Low (&lt;35%)</div>
          <div className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-emerald-400"></span> Moderate (35-59%)</div>
          <div className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-amber-400"></span> High (60-79%)</div>
          <div className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-rose-500"></span> Bottleneck (&ge;80%)</div>
        </div>

        {/* Inspector Modal / Strip */}
        {selectedCell && (
          <div className="bg-slate-50 border border-slate-200 rounded-lg px-4 py-2 flex items-center gap-4">
            <span className="font-bold text-slate-800">{selectedCell.bayCode}</span>
            <span>Congestion: <strong>{selectedCell.congestionPct}%</strong></span>
            <span>Avg Dwell: <strong>{selectedCell.dwellSeconds}s</strong></span>
            <span>SKUs: <strong>{selectedCell.topSkus.join(', ')}</strong></span>
            <button onClick={() => setSelectedCell(null)} className="text-slate-400 hover:text-slate-600 font-bold ml-2">✕</button>
          </div>
        )}
      </div>
    </div>
  );
}

export default WarehouseHeatmap;
