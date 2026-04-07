package com.example.stock.dto;

public class UserUpdateResponse {
    private Long userId;
    private String userName;
    private String email;
    private String role;
    private String updatedAt;

    public UserUpdateResponse(Long userId, String userName, String email, String role, String updatedAt) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.updatedAt = updatedAt;
    }

    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getUpdatedAt() { return updatedAt; }
}