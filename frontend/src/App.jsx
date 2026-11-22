import React, { useState } from 'react';
import Sidebar from './components/Sidebar';
import Navbar from './components/Navbar';
import PnLDashboard from './pages/PnLDashboard';
import ProfitDetective from './pages/ProfitDetective';
import StockShortageRadar from './pages/StockShortageRadar';
import TransferRebalancer from './pages/TransferRebalancer';
import InventoryBarcodeOps from './pages/InventoryBarcodeOps';

export default function App() {
  const [activeTab, setActiveTab] = useState('pnl');
  const [refreshKey, setRefreshKey] = useState(0);

  function handleRefresh() {
    setRefreshKey(k => k + 1);
  }

  return (
    <div className="flex bg-slate-950 min-h-screen text-slate-100">
      <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />

      <div className="flex-1 flex flex-col min-w-0">
        <Navbar onRefresh={handleRefresh} isRefreshing={false} />

        <main className="flex-1 p-6 max-w-7xl w-full mx-auto" key={refreshKey}>
          {activeTab === 'pnl' && <PnLDashboard setActiveTab={setActiveTab} />}
          {activeTab === 'detective' && <ProfitDetective />}
          {activeTab === 'shortage' && <StockShortageRadar />}
          {activeTab === 'transfers' && <TransferRebalancer />}
          {activeTab === 'inventory' && <InventoryBarcodeOps />}
        </main>
      </div>
    </div>
  );
}
