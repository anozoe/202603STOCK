package com.example.stock.dto;

import java.math.BigDecimal;

public class StockOverviewResponse {

    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal per;
    private BigDecimal pbr;
    private BigDecimal roe;
    private BigDecimal dividendYield;

    public StockOverviewResponse(
            BigDecimal openPrice,
            BigDecimal highPrice,
            BigDecimal lowPrice,
            BigDecimal closePrice,
            BigDecimal per,
            BigDecimal pbr,
            BigDecimal roe,
            BigDecimal dividendYield
    ) {
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.per = per;
        this.pbr = pbr;
        this.roe = roe;
        this.dividendYield = dividendYield;
    }

    public BigDecimal getOpenPrice() { return openPrice; }
    public BigDecimal getHighPrice() { return highPrice; }
    public BigDecimal getLowPrice() { return lowPrice; }
    public BigDecimal getClosePrice() { return closePrice; }
    public BigDecimal getPer() { return per; }
    public BigDecimal getPbr() { return pbr; }
    public BigDecimal getRoe() { return roe; }
    public BigDecimal getDividendYield() { return dividendYield; }
}