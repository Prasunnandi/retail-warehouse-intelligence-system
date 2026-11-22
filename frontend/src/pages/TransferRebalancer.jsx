import React, { useState, useEffect } from 'react';
import { ArrowLeftRight, CheckCircle2, AlertTriangle, Building2, Package, RefreshCw } from 'lucide-react';
import { api } from '../services/api';

export default function TransferRebalancer() {
  const [recommendations, setRecommendations] = useState([]);
  const [transfers, setTransfers] = useState([]);
  const [executingId, setExecutingId] = useState(null);

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    const [rec, tr] = await Promise.all([
      api.getTransferRecommendations(),
      api.getTransfers()
    ]);
    setRecommendations(rec);
    setTransfers(tr);
  }

  async function handleExecute(rec) {
    setExecutingId(rec.skuId);
    await api.executeTransfer({
      transferNumber: `TRF-${Math.floor(10000 + Math.random() * 90000)}`,
      sourceBranch: { id: rec.sourceBranchId },
      destinationBranch: { id: rec.destinationBranchId },
      productSku: { id: rec.skuId },
      quantity: rec.suggestedTransferUnits,
      status: 'IN_TRANSIT',
      rationale: rec.rationale
    });
    setExecutingId(null);
    loadData();
  }

  return (
    <div className="space-y-6">
      <div className="p-6 rounded-2xl bg-gradient-to-r from-slate-900 via-emerald-950/20 to-slate-900 border border-emerald-500/30">
        <div className="flex items-center gap-2 text-emerald-400 mb-2">
          <ArrowLeftRight className="h-5 w-5" />
          <span className="text-xs font-bold uppercase tracking-wider">Automated Inventory Balancing</span>
        </div>
        <h2 className="text-2xl font-black text-white tracking-tight">
          Inter-Branch Stock Transfer Recommender
        </h2>
        <p className="text-slate-400 text-sm mt-1 max-w-2xl leading-relaxed">
          Matches branches facing immediate stockout with regional distribution warehouses holding inventory surpluses, rebalancing stock without incurring supplier procurement lead times.
        </p>
      </div>

      {/* Recommendations Cards */}
      <div className="space-y-4">
        <h3 className="font-bold text-white text-base">Active Transfer Recommendations ({recommendations.length})</h3>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {recommendations.map((r, idx) => (
            <div key={idx} className="p-5 rounded-2xl bg-slate-900 border border-emerald-500/30 space-y-4">
              <div className="flex items-start justify-between">
                <div>
                  <h4 className="font-extrabold text-white text-base">{r.title}</h4>
                  <p className="text-xs font-mono text-slate-400 mt-0.5">{r.sku}</p>
                </div>
                <span className={`px-2.5 py-1 rounded-full text-[10px] font-extrabold uppercase tracking-wider ${
                  r.urgency === 'CRITICAL' ? 'bg-rose-500/20 text-rose-300 border border-rose-500/30' : 'bg-amber-500/20 text-amber-300'
                }`}>
                  {r.urgency} Urgency
                </span>
              </div>

              {/* Source vs Target Flow */}
              <div className="p-3.5 rounded-xl bg-slate-950/80 border border-slate-800 text-xs space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-slate-400 font-semibold">From Source (Surplus):</span>
                  <span className="text-white font-bold">{r.sourceBranchName} ({r.sourceSurplusStock} units)</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-slate-400 font-semibold">To Destination (Deficit):</span>
                  <span className="text-rose-400 font-bold">{r.destinationBranchName} ({r.destinationDeficitStock} units)</span>
                </div>
                <div className="pt-2 border-t border-slate-800/80 flex items-center justify-between text-emerald-400 font-mono font-bold">
                  <span>Recommended Transfer:</span>
                  <span>+{r.suggestedTransferUnits} units</span>
                </div>
              </div>

              <p className="text-xs text-slate-400 italic">
                "{r.rationale}"
              </p>

              <button
                onClick={() => handleExecute(r)}
                disabled={executingId === r.skuId}
                className="w-full py-2.5 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-bold transition flex items-center justify-center gap-2 shadow-lg shadow-emerald-600/20"
              >
                {executingId === r.skuId ? <RefreshCw className="h-4 w-4 animate-spin" /> : <ArrowLeftRight className="h-4 w-4" />}
                <span>Authorize & Dispatch Transfer</span>
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* Completed & In-Transit Transfers */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 overflow-hidden">
        <div className="p-4 border-b border-slate-800">
          <h3 className="font-bold text-white text-sm">Transfer History Ledger</h3>
          <p className="text-xs text-slate-400">Inter-branch shipments in transit and completed</p>
        </div>

        <table className="w-full text-left text-xs">
          <thead>
            <tr className="border-b border-slate-800 text-slate-400 bg-slate-950/40">
              <th className="p-4 font-semibold">Transfer #</th>
              <th className="p-4 font-semibold">Source Branch</th>
              <th className="p-4 font-semibold">Destination Branch</th>
              <th className="p-4 font-semibold">Quantity</th>
              <th className="p-4 font-semibold text-right">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/60">
            {transfers.length === 0 ? (
              <tr>
                <td colSpan={5} className="p-6 text-center text-slate-500">
                  No previous transfers logged. Authorize a recommended transfer above to execute!
                </td>
              </tr>
            ) : (
              transfers.map((t) => (
                <tr key={t.id} className="hover:bg-slate-800/40 transition">
                  <td className="p-4 font-mono font-bold text-slate-200">{t.transferNumber}</td>
                  <td className="p-4 text-slate-300">{t.sourceBranch?.name}</td>
                  <td className="p-4 text-slate-300">{t.destinationBranch?.name}</td>
                  <td className="p-4 font-mono font-bold text-emerald-400">+{t.quantity} units</td>
                  <td className="p-4 text-right">
                    <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-emerald-500/20 text-emerald-300">
                      {t.status}
                    </span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
