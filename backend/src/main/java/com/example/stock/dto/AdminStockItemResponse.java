package com.example.stock.dto;

public class AdminStockItemResponse {

    private Long id;
    private String tickerCode;
    private String stockName;
    private Integer displayOrder;

    public AdminStockItemResponse(Long id, String tickerCode, String stockName, Integer displayOrder) {
        this.id = id;
        this.tickerCode = tickerCode;
        this.stockName = stockName;
        this.displayOrder = displayOrder;
    }

    public Long getId() { return id; }
    public String getTickerCode() { return tickerCode; }
    public String getStockName() { return stockName; }
    public Integer getDisplayOrder() { return displayOrder; }
}