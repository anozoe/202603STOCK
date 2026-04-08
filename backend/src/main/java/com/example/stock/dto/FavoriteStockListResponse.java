package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FavoriteStockListResponse {
    private long totalFavorites;
    private int page;
    private int size;
    private int totalPages;
    private long currentFavoriteCount;
    private long maxFavoriteCount;
    private List<StockListItemResponse> items;
}