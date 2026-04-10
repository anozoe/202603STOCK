package com.example.stock.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FmpMarketCapResponse {
    private String symbol;
    private BigDecimal marketCap;
}