package com.example.stock.dto;

public class UserInfoResponse {
    private Long userId;
    private String userName;
    private String email;
    private String role;

    public UserInfoResponse(Long userId, String userName, String email, String role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}