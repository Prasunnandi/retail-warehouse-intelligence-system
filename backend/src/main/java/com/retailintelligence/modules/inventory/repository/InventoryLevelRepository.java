package com.retailintelligence.modules.inventory.repository;

import com.retailintelligence.modules.inventory.entity.InventoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryLevelRepository extends JpaRepository<InventoryLevel, Long> {
    List<InventoryLevel> findByStoreBranchId(Long branchId);
    List<InventoryLevel> findByProductSkuId(Long skuId);
    Optional<InventoryLevel> findByStoreBranchIdAndProductSkuId(Long branchId, Long skuId);
}
