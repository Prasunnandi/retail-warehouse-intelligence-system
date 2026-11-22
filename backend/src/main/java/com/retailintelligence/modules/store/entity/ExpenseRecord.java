package com.retailintelligence.modules.store.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "store_expenses")
public class ExpenseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id")
    private StoreBranch storeBranch;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @Column(nullable = false)
    private Double amount;

    private String description;

    public enum ExpenseCategory {
        RENT,
        SALARIES,
        UTILITIES,
        LOGISTICS_FREIGHT,
        MARKETING,
        STORE_MAINTENANCE
    }

    public ExpenseRecord() {}

    public ExpenseRecord(StoreBranch storeBranch, LocalDate expenseDate, ExpenseCategory category, Double amount, String description) {
        this.storeBranch = storeBranch;
        this.expenseDate = expenseDate;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StoreBranch getStoreBranch() { return storeBranch; }
    public void setStoreBranch(StoreBranch storeBranch) { this.storeBranch = storeBranch; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public ExpenseCategory getCategory() { return category; }
    public void setCategory(ExpenseCategory category) { this.category = category; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
