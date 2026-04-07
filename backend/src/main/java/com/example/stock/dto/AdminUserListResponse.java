package com.example.stock.dto;

import java.util.List;

public class AdminUserListResponse {

    private long totalCount;
    private long maxDisplayCount;
    private List<AdminUserItemResponse> items;

    public AdminUserListResponse(long totalCount, long maxDisplayCount, List<AdminUserItemResponse> items) {
        this.totalCount = totalCount;
        this.maxDisplayCount = maxDisplayCount;
        this.items = items;
    }

    public long getTotalCount() { return totalCount; }
    public long getMaxDisplayCount() { return maxDisplayCount; }
    public List<AdminUserItemResponse> getItems() { return items; }
}