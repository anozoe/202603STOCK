package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String userName;
    private String email;
    private String role;
    private String updatedAt;
}