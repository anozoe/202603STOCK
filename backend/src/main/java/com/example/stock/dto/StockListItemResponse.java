package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockListItemResponse {
    private String tickerCode;
    private String stockName;
    private Integer market;
    private BigDecimal currentPrice;
    private BigDecimal priceChange;
    private BigDecimal changeRate;
    private Long marketCap;
    private boolean favorite;
}