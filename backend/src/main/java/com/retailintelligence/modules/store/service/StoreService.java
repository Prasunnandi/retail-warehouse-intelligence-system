package com.retailintelligence.modules.store.service;

import com.retailintelligence.modules.store.entity.ExpenseRecord;
import com.retailintelligence.modules.store.entity.StoreBranch;
import com.retailintelligence.modules.store.repository.ExpenseRecordRepository;
import com.retailintelligence.modules.store.repository.StoreBranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreService {

    private final StoreBranchRepository branchRepository;
    private final ExpenseRecordRepository expenseRepository;

    public StoreService(StoreBranchRepository branchRepository, ExpenseRecordRepository expenseRepository) {
        this.branchRepository = branchRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<StoreBranch> getAllBranches() {
        return branchRepository.findAll();
    }

    public StoreBranch createBranch(StoreBranch branch) {
        return branchRepository.save(branch);
    }

    public List<ExpenseRecord> getExpensesByBranch(Long branchId) {
        return expenseRepository.findByStoreBranchId(branchId);
    }

    public List<ExpenseRecord> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public ExpenseRecord recordExpense(ExpenseRecord expense) {
        return expenseRepository.save(expense);
    }
}
