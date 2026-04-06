package com.example.stock.dto;

import java.math.BigDecimal;

public class FavoriteStockItemResponse {
    private String tickerCode;
    private String stockName;
    private Integer market;
    private BigDecimal currentPrice;
    private BigDecimal priceChange;
    private BigDecimal changeRate;
    private Long marketCap;
    private boolean favorite;

    public FavoriteStockItemResponse(
            String tickerCode,
            String stockName,
            Integer market,
            BigDecimal currentPrice,
            BigDecimal priceChange,
            BigDecimal changeRate,
            Long marketCap,
            boolean favorite
    ) {
        this.tickerCode = tickerCode;
        this.stockName = stockName;
        this.market = market;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.changeRate = changeRate;
        this.marketCap = marketCap;
        this.favorite = favorite;
    }

    public String getTickerCode() { return tickerCode; }
    public String getStockName() { return stockName; }
    public Integer getMarket() { return market; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public BigDecimal getPriceChange() { return priceChange; }
    public BigDecimal getChangeRate() { return changeRate; }
    public Long getMarketCap() { return marketCap; }
    public boolean isFavorite() { return favorite; }
}