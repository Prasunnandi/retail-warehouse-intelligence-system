package com.retailintelligence.modules.inventory.entity;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.store.entity.StoreBranch;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_levels", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"branch_id", "sku_id"})
})
public class InventoryLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id")
    private StoreBranch storeBranch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sku_id")
    private ProductSku productSku;

    @Column(nullable = false)
    private Integer quantityOnHand;

    private Integer quantityReserved;
    private String binLocation;
    private LocalDateTime lastAuditDate;

    public InventoryLevel() {}

    public InventoryLevel(StoreBranch storeBranch, ProductSku productSku, Integer quantityOnHand,
                          Integer quantityReserved, String binLocation, LocalDateTime lastAuditDate) {
        this.storeBranch = storeBranch;
        this.productSku = productSku;
        this.quantityOnHand = quantityOnHand;
        this.quantityReserved = quantityReserved;
        this.binLocation = binLocation;
        this.lastAuditDate = lastAuditDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StoreBranch getStoreBranch() { return storeBranch; }
    public void setStoreBranch(StoreBranch storeBranch) { this.storeBranch = storeBranch; }

    public ProductSku getProductSku() { return productSku; }
    public void setProductSku(ProductSku productSku) { this.productSku = productSku; }

    public Integer getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(Integer quantityOnHand) { this.quantityOnHand = quantityOnHand; }

    public Integer getQuantityReserved() { return quantityReserved; }
    public void setQuantityReserved(Integer quantityReserved) { this.quantityReserved = quantityReserved; }

    public String getBinLocation() { return binLocation; }
    public void setBinLocation(String binLocation) { this.binLocation = binLocation; }

    public LocalDateTime getLastAuditDate() { return lastAuditDate; }
    public void setLastAuditDate(LocalDateTime lastAuditDate) { this.lastAuditDate = lastAuditDate; }
}
