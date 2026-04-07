package com.example.stock.dto;

import java.util.List;

public class StockListResponse {

    private long totalCount;
    private long currentFavoriteCount;
    private long maxFavoriteCount;
    private List<StockListItemResponse> items;

    public StockListResponse(
            long totalCount,
            long currentFavoriteCount,
            long maxFavoriteCount,
            List<StockListItemResponse> items
    ) {
        this.totalCount = totalCount;
        this.currentFavoriteCount = currentFavoriteCount;
        this.maxFavoriteCount = maxFavoriteCount;
        this.items = items;
    }

    public long getTotalCount() { return totalCount; }
    public long getCurrentFavoriteCount() { return currentFavoriteCount; }
    public long getMaxFavoriteCount() { return maxFavoriteCount; }
    public List<StockListItemResponse> getItems() { return items; }
}