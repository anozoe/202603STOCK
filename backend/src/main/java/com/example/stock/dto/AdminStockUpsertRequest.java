package com.example.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminStockUpsertRequest {

    private Long id;

    @NotBlank
    @Size(max = 20)
    private String tickerCode;

    @NotBlank
    @Size(max = 100)
    private String stockName;

    @NotNull
    private Integer market;

    @NotNull
    private BigDecimal currentPrice;

    private BigDecimal priceChange;
    private BigDecimal changeRate;
    private Long marketCap;

    @NotNull
    private Integer displayOrder;
}