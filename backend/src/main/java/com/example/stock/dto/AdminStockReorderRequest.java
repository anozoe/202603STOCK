package com.example.stock.dto;

import java.util.List;

public class AdminStockReorderRequest {

    private List<Long> stockIds;

    public List<Long> getStockIds() { return stockIds; }
    public void setStockIds(List<Long> stockIds) { this.stockIds = stockIds; }
}