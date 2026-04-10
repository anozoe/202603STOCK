package com.example.stock.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FmpKeyMetricsTtmResponse {
    private String symbol;
    private BigDecimal dividendPerShareTTM;
    private BigDecimal bookValuePerShareTTM;
}