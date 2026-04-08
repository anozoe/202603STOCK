package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StockListResponse {
    private int totalCount;
    private int page;
    private int size;
    private int totalPages;
    private int currentFavoriteCount;
    private int maxFavoriteCount;
    private List<StockListItemResponse> items;
}