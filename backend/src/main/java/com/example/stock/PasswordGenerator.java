package com.example.stock;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "Test1234!";
        String hashed = encoder.encode(rawPassword);

        System.out.println("ハッシュ: " + hashed);
    }
}