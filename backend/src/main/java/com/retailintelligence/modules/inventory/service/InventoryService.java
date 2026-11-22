package com.retailintelligence.modules.inventory.service;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.catalog.repository.ProductSkuRepository;
import com.retailintelligence.modules.inventory.entity.*;
import com.retailintelligence.modules.inventory.repository.*;
import com.retailintelligence.modules.store.entity.StoreBranch;
import com.retailintelligence.modules.store.repository.StoreBranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    private final InventoryLevelRepository inventoryRepository;
    private final StockTransferRepository transferRepository;
    private final PurchaseOrderRepository poRepository;
    private final StoreBranchRepository branchRepository;
    private final ProductSkuRepository skuRepository;

    public InventoryService(InventoryLevelRepository inventoryRepository,
                            StockTransferRepository transferRepository,
                            PurchaseOrderRepository poRepository,
                            StoreBranchRepository branchRepository,
                            ProductSkuRepository skuRepository) {
        this.inventoryRepository = inventoryRepository;
        this.transferRepository = transferRepository;
        this.poRepository = poRepository;
        this.branchRepository = branchRepository;
        this.skuRepository = skuRepository;
    }

    public List<InventoryLevel> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public List<InventoryLevel> getInventoryByBranch(Long branchId) {
        return inventoryRepository.findByStoreBranchId(branchId);
    }

    public InventoryLevel adjustStock(Long branchId, Long skuId, int quantityDelta, String binLocation) {
        Optional<InventoryLevel> existing = inventoryRepository.findByStoreBranchIdAndProductSkuId(branchId, skuId);
        InventoryLevel level;
        if (existing.isPresent()) {
            level = existing.get();
            level.setQuantityOnHand(Math.max(0, level.getQuantityOnHand() + quantityDelta));
            if (binLocation != null && !binLocation.isBlank()) level.setBinLocation(binLocation);
            level.setLastAuditDate(LocalDateTime.now());
        } else {
            StoreBranch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new IllegalArgumentException("Branch not found: " + branchId));
            ProductSku sku = skuRepository.findById(skuId)
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + skuId));
            level = new InventoryLevel(branch, sku, Math.max(0, quantityDelta), 0, binLocation, LocalDateTime.now());
        }
        return inventoryRepository.save(level);
    }

    // Stock Transfers
    public List<StockTransfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    public StockTransfer executeTransfer(StockTransfer transfer) {
        if (transfer.getStatus() == null) {
            transfer.setStatus(StockTransfer.TransferStatus.IN_TRANSIT);
        }
        transfer.setTransferDate(LocalDateTime.now());

        // Deduct from source branch
        adjustStock(transfer.getSourceBranch().getId(), transfer.getProductSku().getId(), -transfer.getQuantity(), null);
        // Add to destination branch
        adjustStock(transfer.getDestinationBranch().getId(), transfer.getProductSku().getId(), transfer.getQuantity(), null);

        transfer.setStatus(StockTransfer.TransferStatus.COMPLETED);
        return transferRepository.save(transfer);
    }

    // Purchase Orders
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return poRepository.findAll();
    }

    public PurchaseOrder createPurchaseOrder(PurchaseOrder po) {
        return poRepository.save(po);
    }
}
