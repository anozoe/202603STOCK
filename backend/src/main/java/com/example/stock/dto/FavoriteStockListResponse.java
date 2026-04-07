package com.example.stock.dto;

import java.util.List;

public class FavoriteStockListResponse {
    private long totalCount;
    private long currentCount;
    private long maxCount;
    private List<FavoriteStockItemResponse> items;

    public FavoriteStockListResponse(long totalCount, long currentCount, long maxCount, List<FavoriteStockItemResponse> items) {
        this.totalCount = totalCount;
        this.currentCount = currentCount;
        this.maxCount = maxCount;
        this.items = items;
    }

    public long getTotalCount() { return totalCount; }
    public long getCurrentCount() { return currentCount; }
    public long getMaxCount() { return maxCount; }
    public List<FavoriteStockItemResponse> getItems() { return items; }
}