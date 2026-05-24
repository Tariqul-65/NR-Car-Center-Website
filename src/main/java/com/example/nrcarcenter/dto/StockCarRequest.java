package com.example.nrcarcenter.dto;

import com.example.nrcarcenter.entity.StockStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockCarRequest {
    private String stockId;
    private String chassisNo;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private StockStatus status;
    private BigDecimal price;
}
