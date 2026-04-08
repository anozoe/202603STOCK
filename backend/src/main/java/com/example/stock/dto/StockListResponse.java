package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StockListResponse {
    private long totalCount;
    private int page;
    private int size;
    private int totalPages;
    private long currentFavoriteCount;
    private long maxFavoriteCount;
    private List<StockListItemResponse> items;
}