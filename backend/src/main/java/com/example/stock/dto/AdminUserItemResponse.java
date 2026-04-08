package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserItemResponse {
    private Long id;
    private String userName;
    private String email;
    private String role;
}