package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class StockDetailResponse {
    private String tickerCode;
    private String stockName;
    private Integer market;
    private BigDecimal currentPrice;
    private BigDecimal priceChange;
    private LocalDateTime fetchedAt;
    private StockOverviewResponse overview;
    private List<StockChartPointResponse> weekChart;
    private List<StockChartPointResponse> monthChart;
}