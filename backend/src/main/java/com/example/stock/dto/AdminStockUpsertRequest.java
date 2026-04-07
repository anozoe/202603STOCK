package com.example.stock.dto;

public class AdminStockUpsertRequest {

    private String tickerCode;
    private String stockName;

    public String getTickerCode() { return tickerCode; }
    public String getStockName() { return stockName; }

    public void setTickerCode(String tickerCode) { this.tickerCode = tickerCode; }
    public void setStockName(String stockName) { this.stockName = stockName; }
}