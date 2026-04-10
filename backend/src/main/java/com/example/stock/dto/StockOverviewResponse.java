package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockOverviewResponse {
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal per;
    private BigDecimal pbr;
    private BigDecimal roe;
    private BigDecimal dividendYield;
    private Long volume;
    private Long marketCap;
}