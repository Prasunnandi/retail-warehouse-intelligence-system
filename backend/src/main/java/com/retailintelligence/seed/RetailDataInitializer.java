package com.retailintelligence.seed;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.catalog.repository.ProductSkuRepository;
import com.retailintelligence.modules.inventory.entity.*;
import com.retailintelligence.modules.inventory.repository.*;
import com.retailintelligence.modules.sales.entity.SalesTransaction;
import com.retailintelligence.modules.sales.repository.SalesTransactionRepository;
import com.retailintelligence.modules.store.entity.*;
import com.retailintelligence.modules.store.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class RetailDataInitializer implements CommandLineRunner {

    private final StoreBranchRepository branchRepository;
    private final ExpenseRecordRepository expenseRepository;
    private final ProductSkuRepository skuRepository;
    private final InventoryLevelRepository inventoryRepository;
    private final SalesTransactionRepository salesRepository;
    private final StockTransferRepository transferRepository;
    private final PurchaseOrderRepository poRepository;

    public RetailDataInitializer(StoreBranchRepository branchRepository,
                                 ExpenseRecordRepository expenseRepository,
                                 ProductSkuRepository skuRepository,
                                 InventoryLevelRepository inventoryRepository,
                                 SalesTransactionRepository salesRepository,
                                 StockTransferRepository transferRepository,
                                 PurchaseOrderRepository poRepository) {
        this.branchRepository = branchRepository;
        this.expenseRepository = expenseRepository;
        this.skuRepository = skuRepository;
        this.inventoryRepository = inventoryRepository;
        this.salesRepository = salesRepository;
        this.transferRepository = transferRepository;
        this.poRepository = poRepository;
    }

    @Override
    public void run(String... args) {
        if (branchRepository.count() > 0) return;

        // 1. Seed Store Branches & Warehouse Hubs
        StoreBranch b1 = branchRepository.save(new StoreBranch("STR-01", "Downtown Michigan Ave Flagship", "Chicago", "600 N Michigan Ave", StoreBranch.BranchType.RETAIL_STORE));
        StoreBranch b2 = branchRepository.save(new StoreBranch("STR-02", "Galleria Mall Store", "Naperville", "1951 W Jefferson Ave", StoreBranch.BranchType.RETAIL_STORE));
        StoreBranch b3 = branchRepository.save(new StoreBranch("STR-03", "Metro Express Outlet", "Evanston", "1620 Sherman Ave", StoreBranch.BranchType.RETAIL_STORE));
        StoreBranch b4 = branchRepository.save(new StoreBranch("HUB-04", "Central Midwest Distribution Hub", "Joliet", "3500 Logistics Blvd", StoreBranch.BranchType.WAREHOUSE_HUB));

        // 2. Seed Operating Expenses
        expenseRepository.save(new ExpenseRecord(b1, LocalDate.now().minusDays(15), ExpenseRecord.ExpenseCategory.RENT, 18500.0, "Prime retail commercial lease"));
        expenseRepository.save(new ExpenseRecord(b1, LocalDate.now().minusDays(10), ExpenseRecord.ExpenseCategory.SALARIES, 14200.0, "Store staff and shift supervisors"));
        expenseRepository.save(new ExpenseRecord(b1, LocalDate.now().minusDays(5), ExpenseRecord.ExpenseCategory.UTILITIES, 2400.0, "Electricity and HVAC"));

        expenseRepository.save(new ExpenseRecord(b2, LocalDate.now().minusDays(15), ExpenseRecord.ExpenseCategory.RENT, 8900.0, "Mall space lease"));
        expenseRepository.save(new ExpenseRecord(b2, LocalDate.now().minusDays(10), ExpenseRecord.ExpenseCategory.SALARIES, 8500.0, "Store associates"));

        expenseRepository.save(new ExpenseRecord(b3, LocalDate.now().minusDays(15), ExpenseRecord.ExpenseCategory.RENT, 5200.0, "Urban storefront lease"));
        expenseRepository.save(new ExpenseRecord(b3, LocalDate.now().minusDays(10), ExpenseRecord.ExpenseCategory.SALARIES, 5800.0, "Outlet team"));

        expenseRepository.save(new ExpenseRecord(b4, LocalDate.now().minusDays(15), ExpenseRecord.ExpenseCategory.RENT, 12000.0, "Industrial warehouse depot lease"));
        expenseRepository.save(new ExpenseRecord(b4, LocalDate.now().minusDays(12), ExpenseRecord.ExpenseCategory.LOGISTICS_FREIGHT, 7600.0, "Inter-branch freight transfer trucks"));

        // 3. Seed SKUs (Including the standout High-Revenue Low-Profit SKU)
        ProductSku s1 = skuRepository.save(new ProductSku("SKU-TV-65", "890123400101", "65-Inch 4K OLED Smart TV",
                "Consumer Electronics", "Unit", 950.0, 980.0, 10)); // Profit margin is only $30 (3%)! High revenue, low profit!
        
        ProductSku s2 = skuRepository.save(new ProductSku("SKU-CAB-01", "890123400102", "Ultra High-Speed HDMI 2.1 Cable",
                "Accessories", "Unit", 4.20, 28.0, 30)); // Profit margin is $23.80 (85%)!

        ProductSku s3 = skuRepository.save(new ProductSku("SKU-COAT-01", "890123400201", "Cashmere Wool Winter Overcoat",
                "Apparel", "Unit", 115.0, 275.0, 15)); // High velocity winter apparel

        ProductSku s4 = skuRepository.save(new ProductSku("SKU-EAR-NC", "890123400103", "Noise Cancelling Wireless Earbuds",
                "Audio", "Unit", 130.0, 220.0, 20));

        ProductSku s5 = skuRepository.save(new ProductSku("SKU-OIL-EV", "890123400301", "Cold-Pressed Extra Virgin Olive Oil 1L",
                "Gourmet Grocery", "Bottle", 7.50, 18.0, 25));

        // 4. Seed Inventory Levels (Simulating stockouts at retail stores and surplus at warehouse)
        LocalDateTime now = LocalDateTime.now();
        // Flagship has critical shortage of Winter Coats (2 units vs 15 min safety), Warehouse Hub has 140!
        inventoryRepository.save(new InventoryLevel(b1, s3, 2, 0, "RACK-A1", now));
        inventoryRepository.save(new InventoryLevel(b4, s3, 140, 10, "ZONE-W-104", now));
        inventoryRepository.save(new InventoryLevel(b2, s3, 18, 0, "RACK-B2", now));

        // Earbuds: Naperville has 1 unit, Warehouse has 95!
        inventoryRepository.save(new InventoryLevel(b2, s4, 1, 0, "SHELF-E3", now));
        inventoryRepository.save(new InventoryLevel(b4, s4, 95, 5, "ZONE-W-201", now));
        inventoryRepository.save(new InventoryLevel(b1, s4, 28, 0, "SHELF-D1", now));

        // 65" TV
        inventoryRepository.save(new InventoryLevel(b1, s1, 12, 0, "BAY-HEAVY-1", now));
        inventoryRepository.save(new InventoryLevel(b2, s1, 8, 0, "BAY-HEAVY-2", now));
        inventoryRepository.save(new InventoryLevel(b4, s1, 60, 0, "ZONE-W-PALLET-09", now));

        // HDMI Cables
        inventoryRepository.save(new InventoryLevel(b1, s2, 75, 0, "PEG-ACC-1", now));
        inventoryRepository.save(new InventoryLevel(b2, s2, 40, 0, "PEG-ACC-3", now));
        inventoryRepository.save(new InventoryLevel(b4, s2, 350, 0, "BIN-BULK-88", now));

        // Olive Oil
        inventoryRepository.save(new InventoryLevel(b1, s5, 32, 0, "AISLE-GROC-4", now));
        inventoryRepository.save(new InventoryLevel(b3, s5, 4, 0, "AISLE-GROC-1", now)); // Understocked
        inventoryRepository.save(new InventoryLevel(b4, s5, 120, 0, "BIN-DRY-12", now));

        // 5. Seed Sales Transactions (Generating the Profit Detective insight!)
        // 65" TV generates massive revenue ($117,600) but only $3,600 profit!
        for (int i = 0; i < 6; i++) {
            salesRepository.save(new SalesTransaction("REC-TV-" + (1000 + i), b1, s1, 20, 980.0, 950.0, 0.0, 19600.0, 600.0, now.minusDays(i * 3)));
        }

        // HDMI Cables generate $15,400 revenue and $13,090 pure profit!
        for (int i = 0; i < 5; i++) {
            salesRepository.save(new SalesTransaction("REC-CAB-" + (2000 + i), b1, s2, 110, 28.0, 4.20, 0.0, 3080.0, 2618.0, now.minusDays(i * 4)));
        }

        // Winter Coats
        for (int i = 0; i < 4; i++) {
            salesRepository.save(new SalesTransaction("REC-COAT-" + (3000 + i), b1, s3, 18, 275.0, 115.0, 0.0, 4950.0, 2880.0, now.minusDays(i * 5)));
            salesRepository.save(new SalesTransaction("REC-COAT-N-" + (3100 + i), b2, s3, 12, 275.0, 115.0, 0.0, 3300.0, 1920.0, now.minusDays(i * 5)));
        }

        // 6. Seed Sample Purchase Order
        poRepository.save(new PurchaseOrder("PO-9910", "Samsung Electronics Logistics", b4, 57000.0,
                PurchaseOrder.POStatus.SUBMITTED, LocalDate.now().minusDays(3), LocalDate.now().plusDays(4)));
    }
}
