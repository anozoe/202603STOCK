package com.example.stock.controller;

import com.example.stock.dto.AdminStockListResponse;
import com.example.stock.dto.AdminStockReorderRequest;
import com.example.stock.dto.AdminStockUpsertRequest;
import com.example.stock.dto.AdminUserListResponse;
import com.example.stock.dto.ApiResponse;
import com.example.stock.dto.ValidationErrorResponse;
import com.example.stock.service.AdminService;
import com.example.stock.service.MessageService;
import com.example.stock.util.ValidationErrorUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MessageService messageService;

    @GetMapping("/stocks")
    public ResponseEntity<ApiResponse<AdminStockListResponse>> getStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("I001", messageService.getMessage("I001"), adminService.getStocks(page, size))
        );
    }

    @PostMapping("/stocks")
    public ResponseEntity<ApiResponse<?>> createStock(
            @Valid @RequestBody AdminStockUpsertRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            ValidationErrorResponse error = ValidationErrorUtil.getFirstError(result, messageService);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(error.getMessageId(), error.getMessage())
            );
        }

        adminService.createStock(request);
        return ResponseEntity.ok(
                ApiResponse.success("I003", messageService.getMessage("I003"), null)
        );
    }

    @PutMapping("/stocks")
    public ResponseEntity<ApiResponse<?>> updateStock(
            @Valid @RequestBody AdminStockUpsertRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            ValidationErrorResponse error = ValidationErrorUtil.getFirstError(result, messageService);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(error.getMessageId(), error.getMessage())
            );
        }

        adminService.updateStock(request);
        return ResponseEntity.ok(
                ApiResponse.success("I005", messageService.getMessage("I005"), null)
            );
    }

    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<ApiResponse<?>> deleteStock(@PathVariable Long id) {
        adminService.deleteStock(id);
        return ResponseEntity.ok(
                ApiResponse.success("I004", messageService.getMessage("I004"), null)
        );
    }

    @PostMapping("/stocks/reorder")
    public ResponseEntity<ApiResponse<?>> reorderStocks(
            @Valid @RequestBody AdminStockReorderRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            ValidationErrorResponse error = ValidationErrorUtil.getFirstError(result, messageService);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(error.getMessageId(), error.getMessage())
            );
        }

        adminService.reorder(request);
        return ResponseEntity.ok(
                ApiResponse.success("I006", messageService.getMessage("I006"), null)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<AdminUserListResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("I001", messageService.getMessage("I001"), adminService.getUsers(page, size))
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long id) {
        adminService.logicalDeleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.success("I004", messageService.getMessage("I004"), null)
        );
    }
}