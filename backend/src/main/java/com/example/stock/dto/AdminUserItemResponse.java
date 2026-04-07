package com.example.stock.dto;

public class AdminUserItemResponse {

    private Long id;
    private String userName;
    private String email;
    private String role;

    public AdminUserItemResponse(Long id, String userName, String email, String role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}