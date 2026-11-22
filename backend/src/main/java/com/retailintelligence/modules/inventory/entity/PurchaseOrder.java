package com.retailintelligence.modules.inventory.entity;

import com.retailintelligence.modules.store.entity.StoreBranch;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String poNumber;

    @Column(nullable = false)
    private String supplierName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_branch_id")
    private StoreBranch destinationBranch;

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private POStatus status;

    private LocalDate orderDate;
    private LocalDate expectedArrivalDate;

    public enum POStatus {
        DRAFT,
        SUBMITTED,
        SHIPPED,
        RECEIVED,
        CANCELLED
    }

    public PurchaseOrder() {}

    public PurchaseOrder(String poNumber, String supplierName, StoreBranch destinationBranch,
                         Double totalAmount, POStatus status, LocalDate orderDate, LocalDate expectedArrivalDate) {
        this.poNumber = poNumber;
        this.supplierName = supplierName;
        this.destinationBranch = destinationBranch;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.expectedArrivalDate = expectedArrivalDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public StoreBranch getDestinationBranch() { return destinationBranch; }
    public void setDestinationBranch(StoreBranch destinationBranch) { this.destinationBranch = destinationBranch; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public POStatus getStatus() { return status; }
    public void setStatus(POStatus status) { this.status = status; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public LocalDate getExpectedArrivalDate() { return expectedArrivalDate; }
    public void setExpectedArrivalDate(LocalDate expectedArrivalDate) { this.expectedArrivalDate = expectedArrivalDate; }
}
