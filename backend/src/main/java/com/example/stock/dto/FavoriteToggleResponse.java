package com.example.stock.dto;

public class FavoriteToggleResponse {

    private boolean favorite;

    public FavoriteToggleResponse(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }
}