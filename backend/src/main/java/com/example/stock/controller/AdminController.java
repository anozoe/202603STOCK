package com.example.stock.controller;

import com.example.stock.dto.*;
import com.example.stock.service.AdminService;
import com.example.stock.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminService adminService;
    private final MessageService messageService;

    public AdminController(AdminService adminService, MessageService messageService) {
        this.adminService = adminService;
        this.messageService = messageService;
    }

    @GetMapping("/stocks")
    public ResponseEntity<ApiResponse<AdminStockListResponse>> getStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(null, null, adminService.getStocks(page, size))
        );
    }

    @PostMapping("/stocks")
    public ResponseEntity<ApiResponse<Object>> createStock(@RequestBody AdminStockUpsertRequest request) {
        adminService.createStock(request);
        return ResponseEntity.ok(
                new ApiResponse<>("I003", messageService.getMessage("I003"), null)
        );
    }

    @PutMapping("/stocks/{id}")
    public ResponseEntity<ApiResponse<Object>> updateStock(
            @PathVariable Long id,
            @RequestBody AdminStockUpsertRequest request
    ) {
        adminService.updateStock(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>("I002", messageService.getMessage("I002"), null)
        );
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteStock(@PathVariable Long id) {
        adminService.deleteStock(id);
        return ResponseEntity.ok(
                new ApiResponse<>("I004", messageService.getMessage("I004"), null)
        );
    }

    @PutMapping("/stocks/reorder")
    public ResponseEntity<ApiResponse<Object>> reorderStocks(@RequestBody AdminStockReorderRequest request) {
        adminService.reorderStocks(request);
        return ResponseEntity.ok(
                new ApiResponse<>("I006", messageService.getMessage("I006"), null)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<AdminUserListResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(null, null, adminService.getUsers(page, size))
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Long id) {
        adminService.logicalDeleteUser(id);
        return ResponseEntity.ok(
                new ApiResponse<>("I004", messageService.getMessage("I004"), null)
        );
    }
}