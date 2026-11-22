package com.retailintelligence.modules.intelligence.service;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.catalog.repository.ProductSkuRepository;
import com.retailintelligence.modules.intelligence.dto.*;
import com.retailintelligence.modules.inventory.entity.InventoryLevel;
import com.retailintelligence.modules.inventory.repository.InventoryLevelRepository;
import com.retailintelligence.modules.sales.entity.SalesTransaction;
import com.retailintelligence.modules.sales.repository.SalesTransactionRepository;
import com.retailintelligence.modules.store.entity.ExpenseRecord;
import com.retailintelligence.modules.store.entity.StoreBranch;
import com.retailintelligence.modules.store.repository.ExpenseRecordRepository;
import com.retailintelligence.modules.store.repository.StoreBranchRepository;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetailIntelligenceService {

    private final ProductSkuRepository skuRepository;
    private final InventoryLevelRepository inventoryRepository;
    private final SalesTransactionRepository salesRepository;
    private final StoreBranchRepository branchRepository;
    private final ExpenseRecordRepository expenseRepository;

    public RetailIntelligenceService(ProductSkuRepository skuRepository,
                                     InventoryLevelRepository inventoryRepository,
                                     SalesTransactionRepository salesRepository,
                                     StoreBranchRepository branchRepository,
                                     ExpenseRecordRepository expenseRepository) {
        this.skuRepository = skuRepository;
        this.inventoryRepository = inventoryRepository;
        this.salesRepository = salesRepository;
        this.branchRepository = branchRepository;
        this.expenseRepository = expenseRepository;
    }

    /**
     * Standout Feature 1: Retail Profit Detective
     * Detects products that generate massive top-line revenue but miniscule bottom-line profit.
     */
    public List<ProfitLeakageProductDto> detectProfitLeakageProducts() {
        List<SalesTransaction> sales = salesRepository.findAll();
        Map<Long, List<SalesTransaction>> salesBySku = sales.stream()
                .collect(Collectors.groupingBy(s -> s.getProductSku().getId()));

        List<ProfitLeakageProductDto> results = new ArrayList<>();

        for (Map.Entry<Long, List<SalesTransaction>> entry : salesBySku.entrySet()) {
            List<SalesTransaction> skuSales = entry.getValue();
            ProductSku sku = skuSales.get(0).getProductSku();

            int totalUnits = skuSales.stream().mapToInt(SalesTransaction::getQuantity).sum();
            double totalRev = skuSales.stream().mapToDouble(SalesTransaction::getNetRevenue).sum();
            double totalProf = skuSales.stream().mapToDouble(SalesTransaction::getNetProfit).sum();

            double marginPct = (totalRev > 0) ? (totalProf / totalRev) * 100.0 : 0.0;

            String category = "BALANCED_GROWTH";
            String rootCause = "Healthy margin contribution aligns with operational targets.";
            String action = "Maintain current replenishment velocity.";

            if (totalRev >= 15000.0 && marginPct < 4.0) {
                category = "HIGH_REVENUE_LOW_PROFIT";
                rootCause = "High consumer demand eroded by excessive promotional discounting and high supplier procurement COGS.";
                action = "Curb discounting frequency, bundle with high-margin accessories, or negotiate supplier rebate tier.";
            } else if (marginPct < 1.0) {
                category = "LOSS_LEADER";
                rootCause = "Selling at or below net landing cost.";
                action = "Audit price elasticity or reposition as promotional gateway SKU.";
            }

            results.add(new ProfitLeakageProductDto(
                    sku.getId(), sku.getSku(), sku.getTitle(), sku.getCategory(),
                    totalUnits, round(totalRev, 2), round(totalProf, 2),
                    round(marginPct, 1), category, rootCause, action
            ));
        }

        // Sort: High-revenue-low-profit anomalies first, then descending by revenue
        results.sort((a, b) -> {
            boolean aLeak = "HIGH_REVENUE_LOW_PROFIT".equals(a.getDiagnosticCategory());
            boolean bLeak = "HIGH_REVENUE_LOW_PROFIT".equals(b.getDiagnosticCategory());
            if (aLeak != bLeak) return aLeak ? -1 : 1;
            return Double.compare(b.getTotalRevenue(), a.getTotalRevenue());
        });

        return results;
    }

    /**
     * Standout Feature 2: StockAlert Demand & Replenishment
     * Predicts stock shortages using Apache Commons Math statistical demand modeling.
     */
    public List<StockShortagePredictionDto> predictStockShortages() {
        List<InventoryLevel> inventory = inventoryRepository.findAll();
        List<SalesTransaction> allSales = salesRepository.findAll();
        List<StockShortagePredictionDto> shortages = new ArrayList<>();

        for (InventoryLevel inv : inventory) {
            ProductSku sku = inv.getProductSku();
            StoreBranch branch = inv.getStoreBranch();

            // Compute historical daily sales velocity using Apache Commons Math
            List<SalesTransaction> branchSkuSales = allSales.stream()
                    .filter(s -> s.getProductSku().getId().equals(sku.getId())
                              && s.getStoreBranch().getId().equals(branch.getId()))
                    .collect(Collectors.toList());

            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (SalesTransaction st : branchSkuSales) {
                stats.addValue(st.getQuantity());
            }

            double dailyDemand = (stats.getN() > 0) ? stats.getMean() : 4.5;
            if (dailyDemand <= 0) dailyDemand = 1.0;

            int currentStock = inv.getQuantityOnHand();
            double daysRemaining = round((double) currentStock / dailyDemand, 1);

            int minSafety = (sku.getMinSafetyStock() != null) ? sku.getMinSafetyStock() : 15;

            String risk = "OPTIMAL";
            int recommendedReorder = 0;

            if (daysRemaining <= 3.0 || currentStock < (minSafety / 2)) {
                risk = "CRITICAL_STOCKOUT";
                recommendedReorder = (int) Math.ceil((dailyDemand * 14) + minSafety - currentStock);
            } else if (daysRemaining <= 7.0 || currentStock <= minSafety) {
                risk = "IMMINENT_DEPLETION";
                recommendedReorder = (int) Math.ceil((dailyDemand * 10) + minSafety - currentStock);
            }

            shortages.add(new StockShortagePredictionDto(
                    sku.getId(), sku.getSku(), sku.getTitle(), branch.getId(), branch.getName(),
                    currentStock, round(dailyDemand, 1), daysRemaining, minSafety, risk,
                    Math.max(0, recommendedReorder)
            ));
        }

        // Sort by most urgent stockout risk
        shortages.sort((a, b) -> Double.compare(a.getDaysOfSupplyRemaining(), b.getDaysOfSupplyRemaining()));
        return shortages;
    }

    /**
     * Standout Feature 3: Inter-Branch Stock Transfer Recommender
     * Recommends transferring stock from surplus branches to branches facing stockout.
     */
    public List<TransferRecommendationDto> recommendInterBranchTransfers() {
        List<InventoryLevel> allInv = inventoryRepository.findAll();
        Map<Long, List<InventoryLevel>> bySku = allInv.stream()
                .collect(Collectors.groupingBy(i -> i.getProductSku().getId()));

        List<TransferRecommendationDto> recommendations = new ArrayList<>();

        for (Map.Entry<Long, List<InventoryLevel>> entry : bySku.entrySet()) {
            List<InventoryLevel> levels = entry.getValue();
            ProductSku sku = levels.get(0).getProductSku();
            int minSafety = (sku.getMinSafetyStock() != null) ? sku.getMinSafetyStock() : 15;

            // Find deficit branches
            List<InventoryLevel> deficits = levels.stream()
                    .filter(i -> i.getQuantityOnHand() < minSafety)
                    .collect(Collectors.toList());

            // Find surplus branches (e.g. warehouse hub or well-stocked store)
            List<InventoryLevel> surpluses = levels.stream()
                    .filter(i -> i.getQuantityOnHand() >= (minSafety * 2))
                    .collect(Collectors.toList());

            for (InventoryLevel deficit : deficits) {
                for (InventoryLevel surplus : surpluses) {
                    if (deficit.getStoreBranch().getId().equals(surplus.getStoreBranch().getId())) continue;

                    int shortageUnits = minSafety - deficit.getQuantityOnHand();
                    int transferableSurplus = surplus.getQuantityOnHand() - minSafety;
                    int unitsToTransfer = Math.min(shortageUnits * 2, transferableSurplus);

                    if (unitsToTransfer > 5) {
                        String urgency = (deficit.getQuantityOnHand() <= 3) ? "CRITICAL" : "MODERATE";
                        String rationale = String.format("Branch '%s' has %d units remaining (< %d safety threshold) while '%s' has surplus of %d units.",
                                deficit.getStoreBranch().getName(), deficit.getQuantityOnHand(), minSafety,
                                surplus.getStoreBranch().getName(), surplus.getQuantityOnHand());

                        recommendations.add(new TransferRecommendationDto(
                                sku.getId(), sku.getSku(), sku.getTitle(),
                                surplus.getStoreBranch().getId(), surplus.getStoreBranch().getName(), surplus.getQuantityOnHand(),
                                deficit.getStoreBranch().getId(), deficit.getStoreBranch().getName(), deficit.getQuantityOnHand(),
                                unitsToTransfer, urgency, rationale
                        ));
                    }
                }
            }
        }

        return recommendations;
    }

    /**
     * Branch-wise Profit & Loss Analytics
     */
    public List<BranchPnLDto> calculateBranchPnL() {
        List<StoreBranch> branches = branchRepository.findAll();
        List<SalesTransaction> sales = salesRepository.findAll();
        List<ExpenseRecord> expenses = expenseRepository.findAll();

        List<BranchPnLDto> pnlList = new ArrayList<>();

        for (StoreBranch b : branches) {
            double revenue = sales.stream()
                    .filter(s -> s.getStoreBranch().getId().equals(b.getId()))
                    .mapToDouble(SalesTransaction::getNetRevenue).sum();

            double cogs = sales.stream()
                    .filter(s -> s.getStoreBranch().getId().equals(b.getId()))
                    .mapToDouble(s -> s.getUnitCostPrice() * s.getQuantity()).sum();

            double grossProfit = revenue - cogs;
            double grossMarginPct = (revenue > 0) ? (grossProfit / revenue) * 100.0 : 0.0;

            double opex = expenses.stream()
                    .filter(e -> e.getStoreBranch().getId().equals(b.getId()))
                    .mapToDouble(ExpenseRecord::getAmount).sum();

            double netProfit = grossProfit - opex;
            double netMarginPct = (revenue > 0) ? (netProfit / revenue) * 100.0 : 0.0;

            String health = "HEALTHY_PROFIT";
            if (netProfit < 0) {
                health = "MARGIN_COMPRESSION";
            } else if (netMarginPct < 5.0) {
                health = "BREAK_EVEN";
            }

            pnlList.add(new BranchPnLDto(
                    b.getId(), b.getBranchCode(), b.getName(), b.getBranchType().name(),
                    round(revenue, 2), round(cogs, 2), round(grossProfit, 2),
                    round(grossMarginPct, 1), round(opex, 2), round(netProfit, 2),
                    round(netMarginPct, 1), health
            ));
        }

        return pnlList;
    }

    private double round(double val, int decimals) {
        if (Double.isNaN(val) || Double.isInfinite(val)) return 0.0;
        return BigDecimal.valueOf(val).setScale(decimals, RoundingMode.HALF_UP).doubleValue();
    }
}
