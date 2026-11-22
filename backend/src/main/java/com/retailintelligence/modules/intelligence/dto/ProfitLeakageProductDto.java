package com.retailintelligence.modules.intelligence.dto;

public class ProfitLeakageProductDto {
    private Long skuId;
    private String sku;
    private String title;
    private String category;
    private int unitsSold;
    private double totalRevenue;
    private double totalProfit;
    private double profitMarginPercentage;
    private String diagnosticCategory; // HIGH_REVENUE_LOW_PROFIT, BALANCED_GROWTH, LOSS_LEADER
    private String rootCauseAnalysis;
    private String strategicAction;

    public ProfitLeakageProductDto() {}

    public ProfitLeakageProductDto(Long skuId, String sku, String title, String category, int unitsSold,
                                  double totalRevenue, double totalProfit, double profitMarginPercentage,
                                  String diagnosticCategory, String rootCauseAnalysis, String strategicAction) {
        this.skuId = skuId;
        this.sku = sku;
        this.title = title;
        this.category = category;
        this.unitsSold = unitsSold;
        this.totalRevenue = totalRevenue;
        this.totalProfit = totalProfit;
        this.profitMarginPercentage = profitMarginPercentage;
        this.diagnosticCategory = diagnosticCategory;
        this.rootCauseAnalysis = rootCauseAnalysis;
        this.strategicAction = strategicAction;
    }

    // Getters and Setters
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUnitsSold() { return unitsSold; }
    public void setUnitsSold(int unitsSold) { this.unitsSold = unitsSold; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }

    public double getProfitMarginPercentage() { return profitMarginPercentage; }
    public void setProfitMarginPercentage(double profitMarginPercentage) { this.profitMarginPercentage = profitMarginPercentage; }

    public String getDiagnosticCategory() { return diagnosticCategory; }
    public void setDiagnosticCategory(String diagnosticCategory) { this.diagnosticCategory = diagnosticCategory; }

    public String getRootCauseAnalysis() { return rootCauseAnalysis; }
    public void setRootCauseAnalysis(String rootCauseAnalysis) { this.rootCauseAnalysis = rootCauseAnalysis; }

    public String getStrategicAction() { return strategicAction; }
    public void setStrategicAction(String strategicAction) { this.strategicAction = strategicAction; }
}
