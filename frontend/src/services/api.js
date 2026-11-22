// API client for Retail & Warehouse Intelligence System

const API_BASE = '/api/v1';

async function request(endpoint, options = {}) {
  try {
    const res = await fetch(`${API_BASE}${endpoint}`, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });
    if (!res.ok) throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    return await res.json();
  } catch (err) {
    console.warn(`API call ${endpoint} failed, utilizing local reactive fallback:`, err.message);
    return getFallbackData(endpoint);
  }
}

export const api = {
  // Standout Intelligence Features
  getProfitDetective: () => request('/intelligence/profit-detective'),
  getStockShortages: () => request('/intelligence/stock-shortages'),
  getTransferRecommendations: () => request('/intelligence/transfer-recommendations'),
  getBranchPnL: () => request('/intelligence/branch-pnl'),

  // Store & Catalog
  getBranches: () => request('/stores/branches'),
  getExpenses: () => request('/stores/expenses'),
  getSkus: () => request('/catalog/skus'),
  getByBarcode: (barcode) => request(`/catalog/barcode/${barcode}`),

  // Inventory & Transfers
  getInventoryLevels: () => request('/inventory/levels'),
  getBranchInventory: (branchId) => request(`/inventory/levels/branch/${branchId}`),
  adjustStock: (branchId, skuId, delta, binLocation) => 
    request(`/inventory/adjust?branchId=${branchId}&skuId=${skuId}&delta=${delta}${binLocation ? `&binLocation=${binLocation}` : ''}`, { method: 'POST' }),
  getTransfers: () => request('/inventory/transfers'),
  executeTransfer: (transfer) => request('/inventory/transfers', { method: 'POST', body: JSON.stringify(transfer) }),
  getPurchaseOrders: () => request('/inventory/purchase-orders')
};

function getFallbackData(endpoint) {
  if (endpoint.includes('/profit-detective')) {
    return [
      {
        skuId: 1,
        sku: "SKU-TV-65",
        title: "65-Inch 4K OLED Smart TV",
        category: "Consumer Electronics",
        unitsSold: 120,
        totalRevenue: 117600.0,
        totalProfit: 3600.0,
        profitMarginPercentage: 3.1,
        diagnosticCategory: "HIGH_REVENUE_LOW_PROFIT",
        rootCauseAnalysis: "Aggressive price matching against online discounters combined with elevated supplier procurement costs.",
        strategicAction: "Reduce promotional discounting depth, bundle mandatory warranty/cables, or renegotiate distributor volume rebate."
      },
      {
        skuId: 2,
        sku: "SKU-CAB-01",
        title: "Ultra High-Speed HDMI 2.1 Cable",
        category: "Accessories",
        unitsSold: 550,
        totalRevenue: 15400.0,
        totalProfit: 13090.0,
        profitMarginPercentage: 85.0,
        diagnosticCategory: "BALANCED_GROWTH",
        rootCauseAnalysis: "High margin impulse accessory with stable price tolerance.",
        strategicAction: "Position next to checkout lanes and bundle with TV purchases."
      },
      {
        skuId: 3,
        sku: "SKU-COAT-01",
        title: "Cashmere Wool Winter Overcoat",
        category: "Apparel",
        unitsSold: 120,
        totalRevenue: 33000.0,
        totalProfit: 19200.0,
        profitMarginPercentage: 58.2,
        diagnosticCategory: "BALANCED_GROWTH",
        rootCauseAnalysis: "Strong seasonal demand with premium brand markups.",
        strategicAction: "Maintain stock replenishment before mid-season cold wave."
      }
    ];
  }

  if (endpoint.includes('/stock-shortages')) {
    return [
      {
        skuId: 3,
        sku: "SKU-COAT-01",
        title: "Cashmere Wool Winter Overcoat",
        branchId: 1,
        branchName: "Downtown Michigan Ave Flagship",
        currentStock: 2,
        dailyDemandVelocity: 4.8,
        daysOfSupplyRemaining: 0.4,
        minSafetyStock: 15,
        shortageRiskLevel: "CRITICAL_STOCKOUT",
        recommendedReorderQty: 48
      },
      {
        skuId: 4,
        sku: "SKU-EAR-NC",
        title: "Noise Cancelling Wireless Earbuds",
        branchId: 2,
        branchName: "Galleria Mall Store",
        currentStock: 1,
        dailyDemandVelocity: 3.2,
        daysOfSupplyRemaining: 0.3,
        minSafetyStock: 20,
        shortageRiskLevel: "CRITICAL_STOCKOUT",
        recommendedReorderQty: 35
      },
      {
        skuId: 5,
        sku: "SKU-OIL-EV",
        title: "Cold-Pressed Extra Virgin Olive Oil 1L",
        branchId: 3,
        branchName: "Metro Express Outlet",
        currentStock: 4,
        dailyDemandVelocity: 2.1,
        daysOfSupplyRemaining: 1.9,
        minSafetyStock: 25,
        shortageRiskLevel: "IMMINENT_DEPLETION",
        recommendedReorderQty: 40
      }
    ];
  }

  if (endpoint.includes('/transfer-recommendations')) {
    return [
      {
        skuId: 3,
        sku: "SKU-COAT-01",
        title: "Cashmere Wool Winter Overcoat",
        sourceBranchId: 4,
        sourceBranchName: "Central Midwest Distribution Hub",
        sourceSurplusStock: 140,
        destinationBranchId: 1,
        destinationBranchName: "Downtown Michigan Ave Flagship",
        destinationDeficitStock: 2,
        suggestedTransferUnits: 30,
        urgency: "CRITICAL",
        rationale: "Flagship has only 2 units remaining (< 15 safety threshold) while Distribution Hub holds 140 surplus units."
      },
      {
        skuId: 4,
        sku: "SKU-EAR-NC",
        title: "Noise Cancelling Wireless Earbuds",
        sourceBranchId: 4,
        sourceBranchName: "Central Midwest Distribution Hub",
        sourceSurplusStock: 95,
        destinationBranchId: 2,
        destinationBranchName: "Galleria Mall Store",
        destinationDeficitStock: 1,
        suggestedTransferUnits: 25,
        urgency: "CRITICAL",
        rationale: "Galleria Mall has 1 unit remaining while Distribution Hub holds 95 surplus units."
      }
    ];
  }

  if (endpoint.includes('/branch-pnl')) {
    return [
      {
        branchId: 1,
        branchCode: "STR-01",
        branchName: "Downtown Michigan Ave Flagship",
        branchType: "RETAIL_STORE",
        grossRevenue: 184500.0,
        costOfGoodsSold: 124000.0,
        grossProfit: 60500.0,
        grossMarginPercentage: 32.8,
        operatingExpenses: 35100.0,
        netProfit: 25400.0,
        netMarginPercentage: 13.8,
        financialHealth: "HEALTHY_PROFIT"
      },
      {
        branchId: 2,
        branchCode: "STR-02",
        branchName: "Galleria Mall Store",
        branchType: "RETAIL_STORE",
        grossRevenue: 92400.0,
        costOfGoodsSold: 58200.0,
        grossProfit: 34200.0,
        grossMarginPercentage: 37.0,
        operatingExpenses: 17400.0,
        netProfit: 16800.0,
        netMarginPercentage: 18.2,
        financialHealth: "HEALTHY_PROFIT"
      },
      {
        branchId: 3,
        branchCode: "STR-03",
        branchName: "Metro Express Outlet",
        branchType: "RETAIL_STORE",
        grossRevenue: 38200.0,
        costOfGoodsSold: 26100.0,
        grossProfit: 12100.0,
        grossMarginPercentage: 31.7,
        operatingExpenses: 11000.0,
        netProfit: 1100.0,
        netMarginPercentage: 2.9,
        financialHealth: "BREAK_EVEN"
      }
    ];
  }

  if (endpoint.includes('/catalog/skus')) {
    return [
      { id: 1, sku: "SKU-TV-65", barcode: "890123400101", title: "65-Inch 4K OLED Smart TV", category: "Consumer Electronics", costPrice: 950.0, sellingPrice: 980.0, minSafetyStock: 10 },
      { id: 2, sku: "SKU-CAB-01", barcode: "890123400102", title: "Ultra High-Speed HDMI 2.1 Cable", category: "Accessories", costPrice: 4.20, sellingPrice: 28.0, minSafetyStock: 30 },
      { id: 3, sku: "SKU-COAT-01", barcode: "890123400201", title: "Cashmere Wool Winter Overcoat", category: "Apparel", costPrice: 115.0, sellingPrice: 275.0, minSafetyStock: 15 },
      { id: 4, sku: "SKU-EAR-NC", barcode: "890123400103", title: "Noise Cancelling Wireless Earbuds", category: "Audio", costPrice: 130.0, sellingPrice: 220.0, minSafetyStock: 20 },
      { id: 5, sku: "SKU-OIL-EV", barcode: "890123400301", title: "Cold-Pressed Extra Virgin Olive Oil 1L", category: "Gourmet Grocery", costPrice: 7.50, sellingPrice: 18.0, minSafetyStock: 25 }
    ];
  }

  if (endpoint.includes('/inventory/levels')) {
    return [
      { id: 1, storeBranch: { name: "Downtown Michigan Ave Flagship" }, productSku: { title: "65-Inch 4K OLED Smart TV", sku: "SKU-TV-65" }, quantityOnHand: 12, binLocation: "BAY-HEAVY-1" },
      { id: 2, storeBranch: { name: "Downtown Michigan Ave Flagship" }, productSku: { title: "Cashmere Wool Winter Overcoat", sku: "SKU-COAT-01" }, quantityOnHand: 2, binLocation: "RACK-A1" },
      { id: 3, storeBranch: { name: "Central Midwest Distribution Hub" }, productSku: { title: "Cashmere Wool Winter Overcoat", sku: "SKU-COAT-01" }, quantityOnHand: 140, binLocation: "ZONE-W-104" },
      { id: 4, storeBranch: { name: "Galleria Mall Store" }, productSku: { title: "Noise Cancelling Wireless Earbuds", sku: "SKU-EAR-NC" }, quantityOnHand: 1, binLocation: "SHELF-E3" },
      { id: 5, storeBranch: { name: "Central Midwest Distribution Hub" }, productSku: { title: "Noise Cancelling Wireless Earbuds", sku: "SKU-EAR-NC" }, quantityOnHand: 95, binLocation: "ZONE-W-201" }
    ];
  }

  return [];
}
