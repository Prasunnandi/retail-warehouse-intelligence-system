package com.retailintelligence.modules.inventory.repository;

import com.retailintelligence.modules.inventory.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    List<StockTransfer> findByStatus(StockTransfer.TransferStatus status);
}
