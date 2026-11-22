package com.retailintelligence.modules.store.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "store_branches")
public class StoreBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String branchCode;

    @Column(nullable = false)
    private String name;

    private String city;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BranchType branchType;

    public enum BranchType {
        RETAIL_STORE,
        WAREHOUSE_HUB,
        REGIONAL_DEPOT
    }

    public StoreBranch() {}

    public StoreBranch(String branchCode, String name, String city, String address, BranchType branchType) {
        this.branchCode = branchCode;
        this.name = name;
        this.city = city;
        this.address = address;
        this.branchType = branchType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BranchType getBranchType() { return branchType; }
    public void setBranchType(BranchType branchType) { this.branchType = branchType; }
}
