package com.example.stock.dto;

public class UserUpdateRequest {
    private String userName;
    private String email;

    public String getUserName() { return userName; }
    public String getEmail() { return email; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setEmail(String email) { this.email = email; }
}