package com.retailintelligence.modules.sales.entity;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.store.entity.StoreBranch;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_transactions")
public class SalesTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String receiptNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id")
    private StoreBranch storeBranch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sku_id")
    private ProductSku productSku;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitSellingPrice;

    @Column(nullable = false)
    private Double unitCostPrice;

    private Double discountAmount;

    @Column(nullable = false)
    private Double netRevenue;

    @Column(nullable = false)
    private Double netProfit;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public SalesTransaction() {}

    public SalesTransaction(String receiptNumber, StoreBranch storeBranch, ProductSku productSku,
                            Integer quantity, Double unitSellingPrice, Double unitCostPrice,
                            Double discountAmount, Double netRevenue, Double netProfit, LocalDateTime timestamp) {
        this.receiptNumber = receiptNumber;
        this.storeBranch = storeBranch;
        this.productSku = productSku;
        this.quantity = quantity;
        this.unitSellingPrice = unitSellingPrice;
        this.unitCostPrice = unitCostPrice;
        this.discountAmount = discountAmount;
        this.netRevenue = netRevenue;
        this.netProfit = netProfit;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public StoreBranch getStoreBranch() { return storeBranch; }
    public void setStoreBranch(StoreBranch storeBranch) { this.storeBranch = storeBranch; }

    public ProductSku getProductSku() { return productSku; }
    public void setProductSku(ProductSku productSku) { this.productSku = productSku; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitSellingPrice() { return unitSellingPrice; }
    public void setUnitSellingPrice(Double unitSellingPrice) { this.unitSellingPrice = unitSellingPrice; }

    public Double getUnitCostPrice() { return unitCostPrice; }
    public void setUnitCostPrice(Double unitCostPrice) { this.unitCostPrice = unitCostPrice; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    public Double getNetRevenue() { return netRevenue; }
    public void setNetRevenue(Double netRevenue) { this.netRevenue = netRevenue; }

    public Double getNetProfit() { return netProfit; }
    public void setNetProfit(Double netProfit) { this.netProfit = netProfit; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
