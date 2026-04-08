package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminStockListResponse {
    private int totalCount;
    private List<AdminStockItemResponse> items;
}