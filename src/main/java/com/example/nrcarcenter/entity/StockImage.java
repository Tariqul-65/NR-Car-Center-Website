package com.example.nrcarcenter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_images", indexes = {
        @Index(name = "idx_stock_image_stock", columnList = "stock_car_id")
})
public class StockImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_car_id", nullable = false)
    @JsonIgnore
    private StockCar stockCar;

    @Column(nullable = false, length = 255)
    private String imagePath;
}
