import React, { useState, useEffect } from 'react';
import { AlertCircle, Clock, ShoppingCart, TrendingUp, CheckCircle, ShieldAlert } from 'lucide-react';
import { api } from '../services/api';

export default function StockShortageRadar() {
  const [shortages, setShortages] = useState([]);

  useEffect(() => {
    api.getStockShortages().then(setShortages);
  }, []);

  return (
    <div className="space-y-6">
      <div className="p-6 rounded-2xl bg-gradient-to-r from-slate-900 via-rose-950/20 to-slate-900 border border-rose-500/30">
        <div className="flex items-center gap-2 text-rose-400 mb-2">
          <AlertCircle className="h-5 w-5" />
          <span className="text-xs font-bold uppercase tracking-wider">StockAlert Replenishment Intelligence</span>
        </div>
        <h2 className="text-2xl font-black text-white tracking-tight">
          Predictive Stock Shortage & Replenishment Radar
        </h2>
        <p className="text-slate-400 text-sm mt-1 max-w-2xl leading-relaxed">
          Analyzes historical sales transactions using Apache Commons Math statistical velocity to compute burn rate, predict stock-out horizons, and calculate suggested reorder quantities.
        </p>
      </div>

      {/* Shortage Grid */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 overflow-hidden">
        <div className="p-4 border-b border-slate-800">
          <h3 className="font-bold text-white text-sm">Store & Warehouse Inventory Depletion Horizon</h3>
          <p className="text-xs text-slate-400">Ranked by days of supply remaining before complete stockout</p>
        </div>

        <table className="w-full text-left text-xs">
          <thead>
            <tr className="border-b border-slate-800 text-slate-400 bg-slate-950/40">
              <th className="p-4 font-semibold">SKU & Product</th>
              <th className="p-4 font-semibold">Store Location</th>
              <th className="p-4 font-semibold">Stock on Hand</th>
              <th className="p-4 font-semibold">Daily Velocity</th>
              <th className="p-4 font-semibold">Supply Horizon</th>
              <th className="p-4 font-semibold">Safety Stock</th>
              <th className="p-4 font-semibold">Recommended PO</th>
              <th className="p-4 font-semibold text-right">Risk Level</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/60">
            {shortages.map((s, idx) => (
              <tr key={idx} className="hover:bg-slate-800/40 transition">
                <td className="p-4">
                  <div className="font-bold text-slate-200">{s.title}</div>
                  <div className="font-mono text-[11px] text-slate-400">{s.sku}</div>
                </td>
                <td className="p-4 text-slate-300 font-medium">{s.branchName}</td>
                <td className="p-4 font-mono font-bold text-white">{s.currentStock} units</td>
                <td className="p-4 font-mono text-blue-400 font-semibold">{s.dailyDemandVelocity} units/day</td>
                <td className="p-4">
                  <span className={`font-mono font-extrabold ${s.daysOfSupplyRemaining < 2 ? 'text-rose-400' : 'text-amber-400'}`}>
                    {s.daysOfSupplyRemaining} days
                  </span>
                </td>
                <td className="p-4 font-mono text-slate-400">{s.minSafetyStock} units</td>
                <td className="p-4 font-mono font-bold text-emerald-400">
                  {s.recommendedReorderQty > 0 ? `+${s.recommendedReorderQty} units` : 'None required'}
                </td>
                <td className="p-4 text-right">
                  <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                    s.shortageRiskLevel === 'CRITICAL_STOCKOUT'
                      ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30 animate-pulse'
                      : 'bg-amber-500/20 text-amber-300'
                  }`}>
                    {s.shortageRiskLevel.replace('_', ' ')}
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
