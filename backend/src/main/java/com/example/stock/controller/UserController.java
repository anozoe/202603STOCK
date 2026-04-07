package com.example.stock.controller;

import com.example.stock.dto.*;
import com.example.stock.service.MessageService;
import com.example.stock.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    public UserController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo() {
        return ResponseEntity.ok(new ApiResponse<>(null, null, userService.getMyInfo()));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyInfo(@RequestBody UserUpdateRequest request) {
        UserUpdateResponse data = userService.updateMyInfo(request);
        return ResponseEntity.ok(new ApiResponse<>("I001", messageService.getMessage("I001"), data));
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<ApiResponse<FavoriteStockListResponse>> getMyFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(new ApiResponse<>(null, null, userService.getMyFavorites(page, size)));
    }

    @DeleteMapping("/me/favorites/{tickerCode}")
    public ResponseEntity<ApiResponse<ResultResponse>> removeFavorite(@PathVariable String tickerCode) {
        ResultResponse data = userService.removeFavorite(tickerCode);
        return ResponseEntity.ok(new ApiResponse<>("I002", messageService.getMessage("I002"), data));
    }
}