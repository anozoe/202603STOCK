package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminUserListResponse {
    private int totalCount;
    private int maxDisplayCount;
    private List<UserInfoResponse> items;
}