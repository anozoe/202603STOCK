package com.example.stock.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FmpIncomeStatementTtmResponse {
    private String symbol;
    private BigDecimal netIncome;
}