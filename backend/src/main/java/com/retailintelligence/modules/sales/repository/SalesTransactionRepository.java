package com.retailintelligence.modules.sales.repository;

import com.retailintelligence.modules.sales.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    List<SalesTransaction> findByStoreBranchId(Long branchId);
    List<SalesTransaction> findByProductSkuId(Long skuId);

    @Query("SELECT SUM(s.netRevenue) FROM SalesTransaction s WHERE s.storeBranch.id = :branchId")
    Double sumRevenueByBranchId(Long branchId);

    @Query("SELECT SUM(s.netProfit) FROM SalesTransaction s WHERE s.storeBranch.id = :branchId")
    Double sumProfitByBranchId(Long branchId);
}
