package com.example.stock.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AdminStockReorderRequest {

    @NotEmpty
    private List<Long> stockIds;
}