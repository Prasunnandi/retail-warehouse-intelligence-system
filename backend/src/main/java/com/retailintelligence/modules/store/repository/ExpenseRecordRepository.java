package com.retailintelligence.modules.store.repository;

import com.retailintelligence.modules.store.entity.ExpenseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRecordRepository extends JpaRepository<ExpenseRecord, Long> {
    List<ExpenseRecord> findByStoreBranchId(Long branchId);

    @Query("SELECT SUM(e.amount) FROM ExpenseRecord e WHERE e.storeBranch.id = :branchId")
    Double sumExpensesByBranchId(Long branchId);
}
