package com.example.stock.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FmpBalanceSheetTtmResponse {
    private String symbol;
    private BigDecimal totalStockholdersEquity;
}