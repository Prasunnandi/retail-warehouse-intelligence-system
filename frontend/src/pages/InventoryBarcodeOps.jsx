import React, { useState, useEffect } from 'react';
import { Barcode, Search, Warehouse, Plus, CheckCircle2 } from 'lucide-react';
import { api } from '../services/api';

export default function InventoryBarcodeOps() {
  const [inventory, setInventory] = useState([]);
  const [barcodeInput, setBarcodeInput] = useState('');
  const [scannedItem, setScannedItem] = useState(null);
  const [scanMessage, setScanMessage] = useState('');

  useEffect(() => {
    loadInventory();
  }, []);

  async function loadInventory() {
    const data = await api.getInventoryLevels();
    setInventory(data);
  }

  async function handleBarcodeScan(e) {
    e.preventDefault();
    if (!barcodeInput) return;
    const item = await api.getByBarcode(barcodeInput);
    if (item && item.id) {
      setScannedItem(item);
      setScanMessage(`Verified SKU: ${item.title}`);
    } else {
      setScannedItem(null);
      setScanMessage('Barcode not found in catalog');
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-white tracking-tight flex items-center gap-2">
            <Warehouse className="h-5 w-5 text-blue-400" />
            <span>WarehouseIQ Inventory & Barcode Operations</span>
          </h2>
          <p className="text-xs text-slate-400 mt-0.5">
            Physical stock allocation, warehouse bin locations, and barcode-based inventory audits.
          </p>
        </div>
      </div>

      {/* Barcode Scanner Simulator */}
      <div className="p-5 rounded-2xl bg-slate-900 border border-slate-800 space-y-4">
        <div className="flex items-center gap-2">
          <Barcode className="h-5 w-5 text-blue-400" />
          <h3 className="font-bold text-white text-sm">Quick Barcode Scanner Simulator</h3>
        </div>

        <form onSubmit={handleBarcodeScan} className="flex gap-2">
          <input
            type="text"
            placeholder="Scan or enter barcode (e.g. 890123400101, 890123400102, 890123400201)..."
            value={barcodeInput}
            onChange={(e) => setBarcodeInput(e.target.value)}
            className="flex-1 bg-slate-950 border border-slate-800 rounded-xl px-4 py-2.5 text-xs text-slate-200 focus:outline-none focus:border-blue-500 font-mono"
          />
          <button
            type="submit"
            className="px-4 py-2.5 rounded-xl bg-blue-600 hover:bg-blue-500 text-white font-bold text-xs transition"
          >
            Scan Barcode
          </button>
        </form>

        {scanMessage && (
          <div className={`p-3 rounded-xl text-xs font-semibold flex items-center gap-2 ${scannedItem ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20' : 'bg-rose-500/10 text-rose-400 border border-rose-500/20'}`}>
            {scannedItem ? <CheckCircle2 className="h-4 w-4" /> : null}
            <span>{scanMessage}</span>
          </div>
        )}

        {scannedItem && (
          <div className="p-4 rounded-xl bg-slate-950 border border-slate-800/80 grid grid-cols-2 md:grid-cols-4 gap-3 text-xs">
            <div>
              <span className="text-slate-500 block">SKU Code</span>
              <span className="font-bold text-white font-mono">{scannedItem.sku}</span>
            </div>
            <div>
              <span className="text-slate-500 block">Selling Price</span>
              <span className="font-bold text-emerald-400 font-mono">${scannedItem.sellingPrice}</span>
            </div>
            <div>
              <span className="text-slate-500 block">Unit Cost</span>
              <span className="font-bold text-slate-300 font-mono">${scannedItem.costPrice}</span>
            </div>
            <div>
              <span className="text-slate-500 block">Min Safety Stock</span>
              <span className="font-bold text-amber-400 font-mono">{scannedItem.minSafetyStock} units</span>
            </div>
          </div>
        )}
      </div>

      {/* Real-time Inventory Table */}
      <div className="rounded-2xl bg-slate-900 border border-slate-800 overflow-hidden">
        <div className="p-4 border-b border-slate-800">
          <h3 className="font-bold text-white text-sm">Real-time Stock Levels & Bin Locations</h3>
          <p className="text-xs text-slate-400">Inventory counts across retail shelves and warehouse pallets</p>
        </div>

        <table className="w-full text-left text-xs">
          <thead>
            <tr className="border-b border-slate-800 text-slate-400 bg-slate-950/40">
              <th className="p-4 font-semibold">SKU & Item</th>
              <th className="p-4 font-semibold">Facility / Branch</th>
              <th className="p-4 font-semibold">Bin Location</th>
              <th className="p-4 font-semibold text-right">Quantity on Hand</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800/60">
            {inventory.map((inv) => (
              <tr key={inv.id} className="hover:bg-slate-800/40 transition">
                <td className="p-4">
                  <div className="font-bold text-slate-200">{inv.productSku?.title}</div>
                  <div className="font-mono text-[11px] text-slate-400">{inv.productSku?.sku}</div>
                </td>
                <td className="p-4 text-slate-300 font-medium">{inv.storeBranch?.name}</td>
                <td className="p-4 font-mono text-blue-400 font-semibold">{inv.binLocation || 'UNASSIGNED'}</td>
                <td className="p-4 font-mono font-black text-white text-right">{inv.quantityOnHand} units</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
