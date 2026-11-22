import React, { useState, useEffect } from 'react';
import { DollarSign, TrendingUp, Store, BarChart2, ArrowUpRight } from 'lucide-react';
import { api } from '../services/api';

export default function PnLDashboard({ setActiveTab }) {
  const [pnlList, setPnlList] = useState([]);

  useEffect(() => {
    api.getBranchPnL().then(setPnlList);
  }, []);

  const totalRev = pnlList.reduce((acc, curr) => acc + curr.grossRevenue, 0);
  const totalNet = pnlList.reduce((acc, curr) => acc + curr.netProfit, 0);
  const totalOpex = pnlList.reduce((acc, curr) => acc + curr.operatingExpenses, 0);
  const avgMargin = totalRev > 0 ? ((totalNet / totalRev) * 100).toFixed(1) : '0.0';

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="p-6 rounded-2xl bg-gradient-to-r from-slate-900 via-blue-950/20 to-slate-900 border border-blue-500/30 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <span className="px-2.5 py-1 rounded-full text-xs font-semibold bg-blue-500/10 text-blue-400 border border-blue-500/20">
            StoreOps & Retail Financial Intelligence
          </span>
          <h2 className="text-2xl font-black text-white tracking-tight mt-2">
            Multi-Branch Profit & Loss Command
          </h2>
          <p className="text-slate-400 text-sm mt-1 max-w-xl">
            Real-time branch financial performance tracking revenue, landing COGS, retail operating expenses, and net margins.
          </p>
        </div>
        <button
          onClick={() => setActiveTab('detective')}
          className="px-4 py-2.5 rounded-xl bg-amber-500/10 hover:bg-amber-500/20 border border-amber-500/30 text-amber-300 text-xs font-bold transition flex items-center gap-2 self-start md:self-auto"
        >
          <span>Open Profit Detective ➔</span>
        </button>
      </div>

      {/* KPI Stats */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800">
          <span className="text-xs text-slate-400 font-medium">Network Gross Revenue</span>
          <div className="text-2xl font-black text-white tracking-tight mt-1">${totalRev.toLocaleString()}</div>
          <span className="text-[11px] text-emerald-400 font-semibold mt-1 block">Across all retail stores</span>
        </div>
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800">
          <span className="text-xs text-slate-400 font-medium">Network Net Profit</span>
          <div className="text-2xl font-black text-emerald-400 tracking-tight mt-1">${totalNet.toLocaleString()}</div>
          <span className="text-[11px] text-slate-400 mt-1 block">After COGS and all OPEX</span>
        </div>
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800">
          <span className="text-xs text-slate-400 font-medium">Total Operating Expenses</span>
          <div className="text-2xl font-black text-amber-400 tracking-tight mt-1">${totalOpex.toLocaleString()}</div>
          <span className="text-[11px] text-slate-400 mt-1 block">Rent, payroll, and utilities</span>
        </div>
        <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800">
          <span className="text-xs text-slate-400 font-medium">Consolidated Net Margin</span>
          <div className="text-2xl font-black text-blue-400 tracking-tight mt-1">{avgMargin}%</div>
          <span className="text-[11px] text-emerald-400 font-semibold mt-1 block">Target threshold: &gt; 12.0%</span>
        </div>
      </div>

      {/* Branch P&L Table */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 overflow-hidden">
        <div className="p-4 border-b border-slate-800 flex items-center justify-between">
          <div>
            <h3 className="font-bold text-white text-sm">Branch-wise Profit & Loss Breakdown</h3>
            <p className="text-xs text-slate-400">Financial performance per individual store location</p>
          </div>
        </div>

        <table className="w-full text-left text-xs">
          <thead>
            <tr className="border-b border-slate-800 text-slate-400 bg-slate-950/40">
              <th className="p-4 font-semibold">Branch Code & Name</th>
              <th className="p-4 font-semibold">Type</th>
              <th className="p-4 font-semibold">Gross Revenue</th>
              <th className="p-4 font-semibold">COGS</th>
              <th className="p-4 font-semibold">Gross Margin</th>
              <th className="p-4 font-semibold">OPEX</th>
              <th className="p-4 font-semibold">Net Profit</th>
              <th className="p-4 font-semibold text-right">Financial Health</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/60">
            {pnlList.map((b) => (
              <tr key={b.branchId} className="hover:bg-slate-800/40 transition">
                <td className="p-4">
                  <div className="font-bold text-slate-200">{b.branchName}</div>
                  <div className="font-mono text-[11px] text-slate-400">{b.branchCode}</div>
                </td>
                <td className="p-4 text-slate-400">{b.branchType.replace('_', ' ')}</td>
                <td className="p-4 font-mono font-bold text-white">${b.grossRevenue.toLocaleString()}</td>
                <td className="p-4 font-mono text-slate-300">${b.costOfGoodsSold.toLocaleString()}</td>
                <td className="p-4 font-mono text-blue-400">{b.grossMarginPercentage}%</td>
                <td className="p-4 font-mono text-amber-400">${b.operatingExpenses.toLocaleString()}</td>
                <td className="p-4 font-mono font-black text-emerald-400">${b.netProfit.toLocaleString()}</td>
                <td className="p-4 text-right">
                  <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                    b.financialHealth === 'HEALTHY_PROFIT'
                      ? 'bg-emerald-500/20 text-emerald-300'
                      : 'bg-amber-500/20 text-amber-300'
                  }`}>
                    {b.financialHealth.replace('_', ' ')}
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
