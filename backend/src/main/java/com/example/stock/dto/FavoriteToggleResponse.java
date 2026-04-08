package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteToggleResponse {
    private boolean favorite;
}