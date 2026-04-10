package com.example.stock.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FmpRatiosTtmResponse {
    private String symbol;
    private BigDecimal priceToBookRatioTTM;
    private BigDecimal priceToBookRatio;
}