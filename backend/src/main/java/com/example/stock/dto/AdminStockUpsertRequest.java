package com.example.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminStockUpsertRequest {

    private Long id;

    @NotBlank(message = "E001")
    @Size(max = 20, message = "E003")
    private String tickerCode;

    @NotBlank(message = "E001")
    @Size(max = 100, message = "E003")
    private String stockName;

    @NotNull(message = "E001")
    private Integer market;

    @NotNull(message = "E001")
    private BigDecimal currentPrice;

    private BigDecimal priceChange;
    private BigDecimal changeRate;
    private Long marketCap;

    @NotNull(message = "E001")
    private Integer displayOrder;
}