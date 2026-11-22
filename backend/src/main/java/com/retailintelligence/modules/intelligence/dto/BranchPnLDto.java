package com.retailintelligence.modules.intelligence.dto;

public class BranchPnLDto {
    private Long branchId;
    private String branchCode;
    private String branchName;
    private String branchType;
    private double grossRevenue;
    private double costOfGoodsSold;
    private double grossProfit;
    private double grossMarginPercentage;
    private double operatingExpenses;
    private double netProfit;
    private double netMarginPercentage;
    private String financialHealth; // HEALTHY_PROFIT, BREAK_EVEN, MARGIN_COMPRESSION

    public BranchPnLDto() {}

    public BranchPnLDto(Long branchId, String branchCode, String branchName, String branchType,
                        double grossRevenue, double costOfGoodsSold, double grossProfit,
                        double grossMarginPercentage, double operatingExpenses, double netProfit,
                        double netMarginPercentage, String financialHealth) {
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchType = branchType;
        this.grossRevenue = grossRevenue;
        this.costOfGoodsSold = costOfGoodsSold;
        this.grossProfit = grossProfit;
        this.grossMarginPercentage = grossMarginPercentage;
        this.operatingExpenses = operatingExpenses;
        this.netProfit = netProfit;
        this.netMarginPercentage = netMarginPercentage;
        this.financialHealth = financialHealth;
    }

    // Getters and Setters
    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getBranchType() { return branchType; }
    public void setBranchType(String branchType) { this.branchType = branchType; }

    public double getGrossRevenue() { return grossRevenue; }
    public void setGrossRevenue(double grossRevenue) { this.grossRevenue = grossRevenue; }

    public double getCostOfGoodsSold() { return costOfGoodsSold; }
    public void setCostOfGoodsSold(double costOfGoodsSold) { this.costOfGoodsSold = costOfGoodsSold; }

    public double getGrossProfit() { return grossProfit; }
    public void setGrossProfit(double grossProfit) { this.grossProfit = grossProfit; }

    public double getGrossMarginPercentage() { return grossMarginPercentage; }
    public void setGrossMarginPercentage(double grossMarginPercentage) { this.grossMarginPercentage = grossMarginPercentage; }

    public double getOperatingExpenses() { return operatingExpenses; }
    public void setOperatingExpenses(double operatingExpenses) { this.operatingExpenses = operatingExpenses; }

    public double getNetProfit() { return netProfit; }
    public void setNetProfit(double netProfit) { this.netProfit = netProfit; }

    public double getNetMarginPercentage() { return netMarginPercentage; }
    public void setNetMarginPercentage(double netMarginPercentage) { this.netMarginPercentage = netMarginPercentage; }

    public String getFinancialHealth() { return financialHealth; }
    public void setFinancialHealth(String financialHealth) { this.financialHealth = financialHealth; }
}
