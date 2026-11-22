package com.retailintelligence.modules.inventory.controller;

import com.retailintelligence.modules.inventory.entity.*;
import com.retailintelligence.modules.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "WarehouseIQ - Inventory & Transfers", description = "Endpoints for multi-warehouse stock levels, transfers, barcode operations, and POs")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/levels")
    @Operation(summary = "Get inventory levels across all branches and warehouses")
    public ResponseEntity<List<InventoryLevel>> getInventoryLevels() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/levels/branch/{branchId}")
    @Operation(summary = "Get inventory levels for a specific branch")
    public ResponseEntity<List<InventoryLevel>> getBranchInventory(@PathVariable Long branchId) {
        return ResponseEntity.ok(inventoryService.getInventoryByBranch(branchId));
    }

    @PostMapping("/adjust")
    @Operation(summary = "Adjust physical inventory count (Barcode audit / Stock intake)")
    public ResponseEntity<InventoryLevel> adjustStock(@RequestParam Long branchId,
                                                      @RequestParam Long skuId,
                                                      @RequestParam int delta,
                                                      @RequestParam(required = false) String binLocation) {
        return ResponseEntity.ok(inventoryService.adjustStock(branchId, skuId, delta, binLocation));
    }

    @GetMapping("/transfers")
    @Operation(summary = "List all inter-branch stock transfers")
    public ResponseEntity<List<StockTransfer>> getTransfers() {
        return ResponseEntity.ok(inventoryService.getAllTransfers());
    }

    @PostMapping("/transfers")
    @Operation(summary = "Execute an inter-branch stock transfer")
    public ResponseEntity<StockTransfer> executeTransfer(@RequestBody StockTransfer transfer) {
        return ResponseEntity.ok(inventoryService.executeTransfer(transfer));
    }

    @GetMapping("/purchase-orders")
    @Operation(summary = "List all supplier purchase orders")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrders() {
        return ResponseEntity.ok(inventoryService.getAllPurchaseOrders());
    }

    @PostMapping("/purchase-orders")
    @Operation(summary = "Create a new supplier purchase order")
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder po) {
        return ResponseEntity.ok(inventoryService.createPurchaseOrder(po));
    }
}
