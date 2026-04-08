package com.example.stock.controller;

import com.example.stock.dto.*;
import com.example.stock.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stocks")
    public ApiResponse<AdminStockListResponse> getStocks(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return new ApiResponse<>(null, null,
                adminService.getStocks(page, size));
    }

    @PostMapping("/stocks")
    public ApiResponse<ResultResponse> create(
            @RequestBody @Valid AdminStockUpsertRequest req
    ) {
        adminService.createStock(req);
        return new ApiResponse<>(null, "登録しました", null);
    }

    @PutMapping("/stocks")
    public ApiResponse<ResultResponse> update(
            @RequestBody @Valid AdminStockUpsertRequest req
    ) {
        adminService.updateStock(req);
        return new ApiResponse<>(null, "更新しました", null);
    }

    @PostMapping("/stocks/reorder")
    public ApiResponse<ResultResponse> reorder(
            @RequestBody @Valid AdminStockReorderRequest req
    ) {
        adminService.reorder(req);
        return new ApiResponse<>(null, "並び替え完了", null);
    }
}