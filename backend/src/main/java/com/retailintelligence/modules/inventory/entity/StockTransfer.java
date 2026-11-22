package com.retailintelligence.modules.inventory.entity;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.store.entity.StoreBranch;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_transfers")
public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transferNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_branch_id")
    private StoreBranch sourceBranch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dest_branch_id")
    private StoreBranch destinationBranch;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sku_id")
    private ProductSku productSku;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    private LocalDateTime transferDate;
    private String rationale;

    public enum TransferStatus {
        RECOMMENDED,
        APPROVED,
        IN_TRANSIT,
        COMPLETED,
        CANCELLED
    }

    public StockTransfer() {}

    public StockTransfer(String transferNumber, StoreBranch sourceBranch, StoreBranch destinationBranch,
                         ProductSku productSku, Integer quantity, TransferStatus status,
                         LocalDateTime transferDate, String rationale) {
        this.transferNumber = transferNumber;
        this.sourceBranch = sourceBranch;
        this.destinationBranch = destinationBranch;
        this.productSku = productSku;
        this.quantity = quantity;
        this.status = status;
        this.transferDate = transferDate;
        this.rationale = rationale;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTransferNumber() { return transferNumber; }
    public void setTransferNumber(String transferNumber) { this.transferNumber = transferNumber; }

    public StoreBranch getSourceBranch() { return sourceBranch; }
    public void setSourceBranch(StoreBranch sourceBranch) { this.sourceBranch = sourceBranch; }

    public StoreBranch getDestinationBranch() { return destinationBranch; }
    public void setDestinationBranch(StoreBranch destinationBranch) { this.destinationBranch = destinationBranch; }

    public ProductSku getProductSku() { return productSku; }
    public void setProductSku(ProductSku productSku) { this.productSku = productSku; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }

    public LocalDateTime getTransferDate() { return transferDate; }
    public void setTransferDate(LocalDateTime transferDate) { this.transferDate = transferDate; }

    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }
}
