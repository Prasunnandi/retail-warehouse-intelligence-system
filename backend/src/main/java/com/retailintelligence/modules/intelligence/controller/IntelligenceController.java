package com.retailintelligence.modules.intelligence.controller;

import com.retailintelligence.modules.intelligence.dto.*;
import com.retailintelligence.modules.intelligence.service.RetailIntelligenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/intelligence")
@Tag(name = "Retail & Warehouse Intelligence", description = "Endpoints for profit leakage detective, stock shortage forecasting, inter-branch transfers, and branch P&L")
public class IntelligenceController {

    private final RetailIntelligenceService intelligenceService;

    public IntelligenceController(RetailIntelligenceService intelligenceService) {
        this.intelligenceService = intelligenceService;
    }

    @GetMapping("/profit-detective")
    @Operation(summary = "Detect products generating high revenue but low margin (Retail Profit Detective)")
    public ResponseEntity<List<ProfitLeakageProductDto>> getProfitDetective() {
        return ResponseEntity.ok(intelligenceService.detectProfitLeakageProducts());
    }

    @GetMapping("/stock-shortages")
    @Operation(summary = "Predict stockout risks and days of supply remaining (StockAlert)")
    public ResponseEntity<List<StockShortagePredictionDto>> getStockShortages() {
        return ResponseEntity.ok(intelligenceService.predictStockShortages());
    }

    @GetMapping("/transfer-recommendations")
    @Operation(summary = "Recommend stock rebalancing between surplus and deficit branches")
    public ResponseEntity<List<TransferRecommendationDto>> getTransferRecommendations() {
        return ResponseEntity.ok(intelligenceService.recommendInterBranchTransfers());
    }

    @GetMapping("/branch-pnl")
    @Operation(summary = "Branch-wise Profit & Loss (Revenue, COGS, OPEX, Net Margin)")
    public ResponseEntity<List<BranchPnLDto>> getBranchPnL() {
        return ResponseEntity.ok(intelligenceService.calculateBranchPnL());
    }
}
