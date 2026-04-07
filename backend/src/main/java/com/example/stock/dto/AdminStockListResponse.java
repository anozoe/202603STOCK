package com.example.stock.dto;

import java.util.List;

public class AdminStockListResponse {

    private long totalCount;
    private long currentCount;
    private long maxCount;
    private List<AdminStockItemResponse> items;

    public AdminStockListResponse(long totalCount, long currentCount, long maxCount, List<AdminStockItemResponse> items) {
        this.totalCount = totalCount;
        this.currentCount = currentCount;
        this.maxCount = maxCount;
        this.items = items;
    }

    public long getTotalCount() { return totalCount; }
    public long getCurrentCount() { return currentCount; }
    public long getMaxCount() { return maxCount; }
    public List<AdminStockItemResponse> getItems() { return items; }
}

//前のアプリを参考にしたため、件数の拡張だったらLong型の方が良いと思いました。