package com.example.nrcarcenter.repository;

import com.example.nrcarcenter.entity.DeliveredCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveredCarRepository extends JpaRepository<DeliveredCar, Long> {
    List<DeliveredCar> findByChassisNoContainingIgnoreCase(String q);
    List<DeliveredCar> findByCustomerNameContainingIgnoreCase(String q);
    List<DeliveredCar> findByMakeContainingIgnoreCase(String q);
    List<DeliveredCar> findByModelContainingIgnoreCase(String q);
}
