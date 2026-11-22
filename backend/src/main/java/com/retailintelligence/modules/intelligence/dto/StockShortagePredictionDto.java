package com.retailintelligence.modules.intelligence.dto;

public class StockShortagePredictionDto {
    private Long skuId;
    private String sku;
    private String title;
    private Long branchId;
    private String branchName;
    private int currentStock;
    private double dailyDemandVelocity;
    private double daysOfSupplyRemaining;
    private int minSafetyStock;
    private String shortageRiskLevel; // CRITICAL_STOCKOUT, IMMINENT_DEPLETION, OPTIMAL
    private int recommendedReorderQty;

    public StockShortagePredictionDto() {}

    public StockShortagePredictionDto(Long skuId, String sku, String title, Long branchId, String branchName,
                                      int currentStock, double dailyDemandVelocity, double daysOfSupplyRemaining,
                                      int minSafetyStock, String shortageRiskLevel, int recommendedReorderQty) {
        this.skuId = skuId;
        this.sku = sku;
        this.title = title;
        this.branchId = branchId;
        this.branchName = branchName;
        this.currentStock = currentStock;
        this.dailyDemandVelocity = dailyDemandVelocity;
        this.daysOfSupplyRemaining = daysOfSupplyRemaining;
        this.minSafetyStock = minSafetyStock;
        this.shortageRiskLevel = shortageRiskLevel;
        this.recommendedReorderQty = recommendedReorderQty;
    }

    // Getters and Setters
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

    public double getDailyDemandVelocity() { return dailyDemandVelocity; }
    public void setDailyDemandVelocity(double dailyDemandVelocity) { this.dailyDemandVelocity = dailyDemandVelocity; }

    public double getDaysOfSupplyRemaining() { return daysOfSupplyRemaining; }
    public void setDaysOfSupplyRemaining(double daysOfSupplyRemaining) { this.daysOfSupplyRemaining = daysOfSupplyRemaining; }

    public int getMinSafetyStock() { return minSafetyStock; }
    public void setMinSafetyStock(int minSafetyStock) { this.minSafetyStock = minSafetyStock; }

    public String getShortageRiskLevel() { return shortageRiskLevel; }
    public void setShortageRiskLevel(String shortageRiskLevel) { this.shortageRiskLevel = shortageRiskLevel; }

    public int getRecommendedReorderQty() { return recommendedReorderQty; }
    public void setRecommendedReorderQty(int recommendedReorderQty) { this.recommendedReorderQty = recommendedReorderQty; }
}
