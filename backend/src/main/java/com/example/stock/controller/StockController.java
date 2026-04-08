package com.example.stock.controller;

import com.example.stock.dto.ApiResponse;
import com.example.stock.dto.FavoriteStockListResponse;
import com.example.stock.dto.FavoriteToggleResponse;
import com.example.stock.dto.StockDetailResponse;
import com.example.stock.dto.StockListResponse;
import com.example.stock.service.MessageService;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<ApiResponse<StockListResponse>> getStocks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("I001", messageService.getMessage("I001"), stockService.getStocks(keyword, page, size))
        );
    }

    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<FavoriteStockListResponse>> getFavoriteStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("I001", messageService.getMessage("I001"), stockService.getFavoriteStocks(page, size))
        );
    }

    @GetMapping("/{tickerCode}")
    public ResponseEntity<ApiResponse<StockDetailResponse>> getStockDetail(@PathVariable String tickerCode) {
        return ResponseEntity.ok(
                ApiResponse.success("I001", messageService.getMessage("I001"), stockService.getStockDetail(tickerCode))
        );
    }

    @PostMapping("/{tickerCode}/favorite")
    public ResponseEntity<ApiResponse<FavoriteToggleResponse>> addFavorite(@PathVariable String tickerCode) {
        return ResponseEntity.ok(
                ApiResponse.success("I003", messageService.getMessage("I003"), stockService.addFavorite(tickerCode))
        );
    }

    @DeleteMapping("/{tickerCode}/favorite")
    public ResponseEntity<ApiResponse<FavoriteToggleResponse>> removeFavorite(@PathVariable String tickerCode) {
        return ResponseEntity.ok(
                ApiResponse.success("I002", messageService.getMessage("I002"), stockService.removeFavorite(tickerCode))
        );
    }
}