import React, { useState, useEffect } from 'react';
import { Search, AlertTriangle, TrendingDown, DollarSign, ArrowRight, ShieldCheck, Zap } from 'lucide-react';
import { api } from '../services/api';

export default function ProfitDetective() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api.getProfitDetective().then(setProducts);
  }, []);

  const anomalyProducts = products.filter(p => p.diagnosticCategory === 'HIGH_REVENUE_LOW_PROFIT');

  return (
    <div className="space-y-6">
      {/* Banner */}
      <div className="p-6 rounded-2xl bg-gradient-to-r from-slate-900 via-amber-950/25 to-slate-900 border border-amber-500/30">
        <div className="flex items-center gap-2 text-amber-400 mb-2">
          <Search className="h-5 w-5" />
          <span className="text-xs font-bold uppercase tracking-wider">Retail Profit Detective</span>
        </div>
        <h2 className="text-2xl font-black text-white tracking-tight">
          High Revenue vs. Low Margin Leakage Analyzer
        </h2>
        <p className="text-slate-400 text-sm mt-1 max-w-2xl leading-relaxed">
          Identifies products that appear highly successful by top-line sales volume but erode company profitability through deep discounting, high landing costs, or high return rates.
        </p>
      </div>

      {/* Flagged Leakage Cards */}
      <div className="space-y-3">
        <h3 className="font-bold text-white text-base flex items-center gap-2">
          <AlertTriangle className="h-5 w-5 text-amber-400" />
          <span>Flagged Margin Leakage SKUs ({anomalyProducts.length})</span>
        </h3>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {anomalyProducts.map((p) => (
            <div key={p.skuId} className="p-5 rounded-2xl bg-slate-900 border border-amber-500/30 space-y-4">
              <div className="flex items-start justify-between">
                <div>
                  <h4 className="font-extrabold text-white text-base">{p.title}</h4>
                  <p className="text-xs text-slate-400 font-mono mt-0.5">{p.sku} • {p.category}</p>
                </div>
                <span className="px-2.5 py-1 rounded-full text-[10px] font-extrabold uppercase tracking-wider bg-rose-500/20 text-rose-300 border border-rose-500/30">
                  {p.profitMarginPercentage}% Margin
                </span>
              </div>

              <div className="grid grid-cols-3 gap-2 p-3.5 rounded-xl bg-slate-950/80 border border-slate-800 text-xs text-center">
                <div>
                  <span className="text-[11px] text-slate-500 block">Total Revenue</span>
                  <span className="font-black text-white text-sm font-mono">${p.totalRevenue.toLocaleString()}</span>
                </div>
                <div>
                  <span className="text-[11px] text-slate-500 block">Net Profit</span>
                  <span className="font-black text-amber-400 text-sm font-mono">${p.totalProfit.toLocaleString()}</span>
                </div>
                <div>
                  <span className="text-[11px] text-slate-500 block">Units Sold</span>
                  <span className="font-black text-slate-200 text-sm font-mono">{p.unitsSold}</span>
                </div>
              </div>

              <div className="text-xs space-y-2">
                <div>
                  <span className="text-slate-400 font-semibold">Diagnosis: </span>
                  <span className="text-slate-300">{p.rootCauseAnalysis}</span>
                </div>
                <div className="p-3 rounded-lg bg-blue-500/10 border border-blue-500/20">
                  <span className="text-blue-400 font-semibold">Corrective Action: </span>
                  <span className="text-slate-300">{p.strategicAction}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Complete Product Profitability Table */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 overflow-hidden">
        <div className="p-4 border-b border-slate-800">
          <h3 className="font-bold text-white text-sm">Full Product Profitability Matrix</h3>
          <p className="text-xs text-slate-400">Comparing top-line sales against net retained margin</p>
        </div>

        <table className="w-full text-left text-xs">
          <thead>
            <tr className="border-b border-slate-800 text-slate-400 bg-slate-950/40">
              <th className="p-4 font-semibold">Product SKU & Title</th>
              <th className="p-4 font-semibold">Category</th>
              <th className="p-4 font-semibold">Units Sold</th>
              <th className="p-4 font-semibold">Gross Revenue</th>
              <th className="p-4 font-semibold">Net Profit</th>
              <th className="p-4 font-semibold">Profit Margin</th>
              <th className="p-4 font-semibold text-right">Diagnostic Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/60">
            {products.map((p) => (
              <tr key={p.skuId} className="hover:bg-slate-800/40 transition">
                <td className="p-4">
                  <div className="font-bold text-slate-200">{p.title}</div>
                  <div className="font-mono text-[11px] text-slate-400">{p.sku}</div>
                </td>
                <td className="p-4 text-slate-400">{p.category}</td>
                <td className="p-4 font-mono text-slate-300">{p.unitsSold}</td>
                <td className="p-4 font-mono font-bold text-white">${p.totalRevenue.toLocaleString()}</td>
                <td className="p-4 font-mono font-bold text-emerald-400">${p.totalProfit.toLocaleString()}</td>
                <td className="p-4 font-mono font-extrabold text-blue-400">{p.profitMarginPercentage}%</td>
                <td className="p-4 text-right">
                  <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                    p.diagnosticCategory === 'HIGH_REVENUE_LOW_PROFIT'
                      ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30'
                      : 'bg-emerald-500/20 text-emerald-300'
                  }`}>
                    {p.diagnosticCategory.replace('_', ' ')}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
