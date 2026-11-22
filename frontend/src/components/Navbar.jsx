import React from 'react';
import { Search, RefreshCw, Barcode, ShieldCheck } from 'lucide-react';

export default function Navbar({ onRefresh, isRefreshing }) {
  return (
    <header className="h-16 bg-slate-900/80 backdrop-blur border-b border-slate-800 px-6 flex items-center justify-between sticky top-0 z-30">
      <div className="flex items-center gap-3 w-96">
        <div className="relative w-full">
          <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 h-4 w-4 text-slate-500" />
          <input
            type="text"
            placeholder="Scan barcode, search SKU, or branch code..."
            className="w-full pl-10 pr-4 py-2 bg-slate-950/60 border border-slate-800 rounded-xl text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-blue-500/50"
          />
        </div>
      </div>

      <div className="flex items-center gap-4">
        <button
          onClick={onRefresh}
          className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-slate-800 hover:bg-slate-700/80 text-xs font-semibold text-slate-300 transition-colors border border-slate-700/60"
        >
          <RefreshCw className={`h-3.5 w-3.5 text-blue-400 ${isRefreshing ? 'animate-spin' : ''}`} />
          <span>Refresh Analytics</span>
        </button>

        <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-blue-500/10 border border-blue-500/20 text-blue-400 text-xs font-semibold">
          <ShieldCheck className="h-4 w-4 text-blue-400" />
          <span>Multi-Store Synchronized</span>
        </div>
      </div>
    </header>
  );
}
