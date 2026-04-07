package com.example.stock.dto;

import java.math.BigDecimal;
import java.util.List;

public class StockDetailResponse {

    private String tickerCode;
    private String stockName;
    private Integer market;
    private BigDecimal currentPrice;
    private BigDecimal priceChange;
    private String fetchedAt;
    private StockOverviewResponse overview;
    private List<StockChartPointResponse> weekChart;
    private List<StockChartPointResponse> monthChart;

    public StockDetailResponse(
            String tickerCode,
            String stockName,
            Integer market,
            BigDecimal currentPrice,
            BigDecimal priceChange,
            String fetchedAt,
            StockOverviewResponse overview,
            List<StockChartPointResponse> weekChart,
            List<StockChartPointResponse> monthChart
    ) {
        this.tickerCode = tickerCode;
        this.stockName = stockName;
        this.market = market;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.fetchedAt = fetchedAt;
        this.overview = overview;
        this.weekChart = weekChart;
        this.monthChart = monthChart;
    }

    public String getTickerCode() { return tickerCode; }
    public String getStockName() { return stockName; }
    public Integer getMarket() { return market; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public BigDecimal getPriceChange() { return priceChange; }
    public String getFetchedAt() { return fetchedAt; }
    public StockOverviewResponse getOverview() { return overview; }
    public List<StockChartPointResponse> getWeekChart() { return weekChart; }
    public List<StockChartPointResponse> getMonthChart() { return monthChart; }
}