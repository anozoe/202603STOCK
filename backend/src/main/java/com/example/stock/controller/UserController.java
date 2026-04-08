package com.example.stock.controller;

import com.example.stock.dto.ApiResponse;
import com.example.stock.dto.UserInfoResponse;
import com.example.stock.dto.UserUpdateRequest;
import com.example.stock.service.MessageService;
import com.example.stock.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo() {
        return ResponseEntity.ok(
                ApiResponse.success("I001", messageService.getMessage("I001"), userService.getMyInfo())
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateMyInfo(@Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("I005", messageService.getMessage("I005"), userService.updateMyInfo(request))
        );
    }
}