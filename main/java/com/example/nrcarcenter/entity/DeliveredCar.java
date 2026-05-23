package com.example.nrcarcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivered_cars", indexes = {
        @Index(name = "idx_delivered_chassis", columnList = "chassisNo"),
        @Index(name = "idx_delivered_date", columnList = "deliveryDate")
})
public class DeliveredCar extends BaseEntity {

    @Column(nullable = false, length = 64)
    private String chassisNo;

    @Column(nullable = false, length = 120)
    private String make;

    @Column(nullable = false, length = 120)
    private String model;

    @Column(nullable = false, length = 120)
    private String customerName;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal deliveryCost;

    @Column(nullable = true, length = 255)
    private String imagePath;
}
