package com.example.nrcarcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_cars", indexes = {
        @Index(name = "idx_stock_stock_id", columnList = "stockId", unique = true),
        @Index(name = "idx_stock_chassis", columnList = "chassisNo", unique = true),
        @Index(name = "idx_stock_status", columnList = "status")
})
public class StockCar extends BaseEntity {

    @Column(nullable = false, unique = true, length = 32)
    private String stockId;

    @Column(nullable = false, unique = true, length = 64)
    private String chassisNo;

    @Column(nullable = false, length = 120)
    private String make;

    @Column(nullable = false, length = 120)
    private String model;

    @Column(nullable = true, length = 60)
    private String grade;

    @Column(nullable = true, length = 80)
    private String chassisCode;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 60)
    private String color;

    @Column(nullable = true)
    private Integer engineCc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private FuelType fuel;

    @Column(nullable = true)
    private Integer mileage;

    @Column(nullable = true, precision = 4, scale = 1)
    private BigDecimal auctionPoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StockStatus status;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private TransmissionType transmission;

    @Column(nullable = true)
    private Integer seats;

    @Column(nullable = true)
    private Integer doors;

    @OneToMany(mappedBy = "stockCar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StockImage> images = new ArrayList<>();
}
