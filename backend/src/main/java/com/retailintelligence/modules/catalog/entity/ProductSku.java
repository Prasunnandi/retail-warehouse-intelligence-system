package com.retailintelligence.modules.catalog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_skus")
public class ProductSku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(nullable = false)
    private String title;

    private String category;
    private String unit;

    @Column(nullable = false)
    private Double costPrice;

    @Column(nullable = false)
    private Double sellingPrice;

    private Integer minSafetyStock;

    public ProductSku() {}

    public ProductSku(String sku, String barcode, String title, String category, String unit,
                      Double costPrice, Double sellingPrice, Integer minSafetyStock) {
        this.sku = sku;
        this.barcode = barcode;
        this.title = title;
        this.category = category;
        this.unit = unit;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.minSafetyStock = minSafetyStock;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getCostPrice() { return costPrice; }
    public void setCostPrice(Double costPrice) { this.costPrice = costPrice; }

    public Double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(Double sellingPrice) { this.sellingPrice = sellingPrice; }

    public Integer getMinSafetyStock() { return minSafetyStock; }
    public void setMinSafetyStock(Integer minSafetyStock) { this.minSafetyStock = minSafetyStock; }
}
