package com.example.stock.controller;

import com.example.stock.dto.ApiResponse;
import com.example.stock.dto.UserInfoResponse;
import com.example.stock.dto.UserUpdateRequest;
import com.example.stock.dto.ValidationErrorResponse;
import com.example.stock.service.MessageService;
import com.example.stock.service.UserService;
import com.example.stock.util.ValidationErrorUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
                ApiResponse.success(
                        "I001",
                        messageService.getMessage("I001"),
                        userService.getMyInfo()
                )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateMyInfo(
            @Valid @RequestBody UserUpdateRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            ValidationErrorResponse error = ValidationErrorUtil.getFirstError(result, messageService);
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(error.getMessageId(), error.getMessage())
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "I005",
                        messageService.getMessage("I005"),
                        userService.updateMyInfo(request)
                )
        );
    }
}