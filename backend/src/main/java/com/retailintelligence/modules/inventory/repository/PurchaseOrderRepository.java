package com.retailintelligence.modules.inventory.repository;

import com.retailintelligence.modules.inventory.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(PurchaseOrder.POStatus status);
}
