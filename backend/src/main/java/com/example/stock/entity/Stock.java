package com.example.stock.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticker_code", nullable = false, unique = true, length = 20)
    private String tickerCode;

    @Column(name = "stock_name", nullable = false, length = 100)
    private String stockName;

    @Column(name = "market", nullable = false)
    private Integer market;

    @Column(name = "current_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal currentPrice;

    @Column(name = "price_change", precision = 18, scale = 2)
    private BigDecimal priceChange;

    @Column(name = "change_rate", precision = 8, scale = 2)
    private BigDecimal changeRate;

    @Column(name = "market_cap")
    private Long marketCap;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    public Long getId() { return id; }
    public String getTickerCode() { return tickerCode; }
    public String getStockName() { return stockName; }
    public Integer getMarket() { return market; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public BigDecimal getPriceChange() { return priceChange; }
    public BigDecimal getChangeRate() { return changeRate; }
    public Long getMarketCap() { return marketCap; }
    public LocalDateTime getFetchedAt() { return fetchedAt; }

    public void setId(Long id) { this.id = id; }
    public void setTickerCode(String tickerCode) { this.tickerCode = tickerCode; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    public void setMarket(Integer market) { this.market = market; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public void setPriceChange(BigDecimal priceChange) { this.priceChange = priceChange; }
    public void setChangeRate(BigDecimal changeRate) { this.changeRate = changeRate; }
    public void setMarketCap(Long marketCap) { this.marketCap = marketCap; }
    public void setFetchedAt(LocalDateTime fetchedAt) { this.fetchedAt = fetchedAt; }
}