package com.retailintelligence.modules.store.controller;

import com.retailintelligence.modules.store.entity.ExpenseRecord;
import com.retailintelligence.modules.store.entity.StoreBranch;
import com.retailintelligence.modules.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "StoreOps - Multi-Store & Expense Management", description = "Endpoints for managing store branches, warehouse depots, and operating expenditure")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/branches")
    @Operation(summary = "List all store branches and warehouse hubs")
    public ResponseEntity<List<StoreBranch>> getBranches() {
        return ResponseEntity.ok(storeService.getAllBranches());
    }

    @PostMapping("/branches")
    @Operation(summary = "Register a new branch or warehouse depot")
    public ResponseEntity<StoreBranch> createBranch(@RequestBody StoreBranch branch) {
        return ResponseEntity.ok(storeService.createBranch(branch));
    }

    @GetMapping("/expenses")
    @Operation(summary = "List all operating expenses")
    public ResponseEntity<List<ExpenseRecord>> getExpenses() {
        return ResponseEntity.ok(storeService.getAllExpenses());
    }

    @PostMapping("/expenses")
    @Operation(summary = "Log an operational expense for a branch")
    public ResponseEntity<ExpenseRecord> recordExpense(@RequestBody ExpenseRecord expense) {
        return ResponseEntity.ok(storeService.recordExpense(expense));
    }
}
