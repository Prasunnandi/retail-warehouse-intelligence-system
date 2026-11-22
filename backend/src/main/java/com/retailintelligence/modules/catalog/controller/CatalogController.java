package com.retailintelligence.modules.catalog.controller;

import com.retailintelligence.modules.catalog.entity.ProductSku;
import com.retailintelligence.modules.catalog.repository.ProductSkuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
@Tag(name = "Product Catalogue & SKU Management", description = "Endpoints for managing products, barcodes, and pricing")
public class CatalogController {

    private final ProductSkuRepository skuRepository;

    public CatalogController(ProductSkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }

    @GetMapping("/skus")
    @Operation(summary = "List all product SKUs")
    public ResponseEntity<List<ProductSku>> getAllSkus() {
        return ResponseEntity.ok(skuRepository.findAll());
    }

    @GetMapping("/skus/{sku}")
    @Operation(summary = "Look up SKU by code")
    public ResponseEntity<ProductSku> getBySku(@PathVariable String sku) {
        return skuRepository.findBySku(sku)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcode/{barcode}")
    @Operation(summary = "Barcode-based SKU lookup (WarehouseIQ)")
    public ResponseEntity<ProductSku> getByBarcode(@PathVariable String barcode) {
        return skuRepository.findByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/skus")
    @Operation(summary = "Add a new product SKU")
    public ResponseEntity<ProductSku> createSku(@RequestBody ProductSku productSku) {
        return ResponseEntity.ok(skuRepository.save(productSku));
    }
}
