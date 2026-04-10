package com.example.stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminStockUpsertRequest {

    private Long id;

    @NotBlank(message = "E001")
    @Size(max = 20, message = "E003")
    private String tickerCode;
}