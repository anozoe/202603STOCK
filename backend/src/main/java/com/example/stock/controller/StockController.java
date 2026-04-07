package com.example.stock.controller;

import com.example.stock.dto.ApiResponse;
import com.example.stock.dto.FavoriteToggleResponse;
import com.example.stock.dto.StockDetailResponse;
import com.example.stock.dto.StockListResponse;
import com.example.stock.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<StockListResponse>> getStocks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(null, null, stockService.getStocks(keyword, page, size))
        );
    }

    @GetMapping("/{tickerCode}")
    public ResponseEntity<ApiResponse<StockDetailResponse>> getStockDetail(
            @PathVariable String tickerCode
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(null, null, stockService.getStockDetail(tickerCode))
        );
    }

    @PostMapping("/{tickerCode}/favorite")
    public ResponseEntity<ApiResponse<FavoriteToggleResponse>> toggleFavorite(
            @PathVariable String tickerCode
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(null, null, stockService.toggleFavorite(tickerCode))
        );
    }
}