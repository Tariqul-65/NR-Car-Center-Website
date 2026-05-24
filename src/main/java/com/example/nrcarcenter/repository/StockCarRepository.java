package com.example.nrcarcenter.repository;

import com.example.nrcarcenter.entity.StockCar;
import com.example.nrcarcenter.entity.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockCarRepository extends JpaRepository<StockCar, Long> {
    Optional<StockCar> findByStockId(String stockId);
    Optional<StockCar> findByChassisNo(String chassisNo);
    boolean existsByStockId(String stockId);
    boolean existsByChassisNo(String chassisNo);

    List<StockCar> findByStatus(StockStatus status);

    List<StockCar> findByChassisNoContainingIgnoreCase(String q);
    List<StockCar> findByMakeContainingIgnoreCase(String q);
    List<StockCar> findByModelContainingIgnoreCase(String q);
}
