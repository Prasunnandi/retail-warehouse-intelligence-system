package com.retailintelligence.modules.intelligence.dto;

public class TransferRecommendationDto {
    private Long skuId;
    private String sku;
    private String title;
    private Long sourceBranchId;
    private String sourceBranchName;
    private int sourceSurplusStock;
    private Long destinationBranchId;
    private String destinationBranchName;
    private int destinationDeficitStock;
    private int suggestedTransferUnits;
    private String urgency; // CRITICAL, MODERATE
    private String rationale;

    public TransferRecommendationDto() {}

    public TransferRecommendationDto(Long skuId, String sku, String title, Long sourceBranchId,
                                      String sourceBranchName, int sourceSurplusStock, Long destinationBranchId,
                                      String destinationBranchName, int destinationDeficitStock,
                                      int suggestedTransferUnits, String urgency, String rationale) {
        this.skuId = skuId;
        this.sku = sku;
        this.title = title;
        this.sourceBranchId = sourceBranchId;
        this.sourceBranchName = sourceBranchName;
        this.sourceSurplusStock = sourceSurplusStock;
        this.destinationBranchId = destinationBranchId;
        this.destinationBranchName = destinationBranchName;
        this.destinationDeficitStock = destinationDeficitStock;
        this.suggestedTransferUnits = suggestedTransferUnits;
        this.urgency = urgency;
        this.rationale = rationale;
    }

    // Getters and Setters
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getSourceBranchId() { return sourceBranchId; }
    public void setSourceBranchId(Long sourceBranchId) { this.sourceBranchId = sourceBranchId; }

    public String getSourceBranchName() { return sourceBranchName; }
    public void setSourceBranchName(String sourceBranchName) { this.sourceBranchName = sourceBranchName; }

    public int getSourceSurplusStock() { return sourceSurplusStock; }
    public void setSourceSurplusStock(int sourceSurplusStock) { this.sourceSurplusStock = sourceSurplusStock; }

    public Long getDestinationBranchId() { return destinationBranchId; }
    public void setDestinationBranchId(Long destinationBranchId) { this.destinationBranchId = destinationBranchId; }

    public String getDestinationBranchName() { return destinationBranchName; }
    public void setDestinationBranchName(String destinationBranchName) { this.destinationBranchName = destinationBranchName; }

    public int getDestinationDeficitStock() { return destinationDeficitStock; }
    public void setDestinationDeficitStock(int destinationDeficitStock) { this.destinationDeficitStock = destinationDeficitStock; }

    public int getSuggestedTransferUnits() { return suggestedTransferUnits; }
    public void setSuggestedTransferUnits(int suggestedTransferUnits) { this.suggestedTransferUnits = suggestedTransferUnits; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }

    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }
}
