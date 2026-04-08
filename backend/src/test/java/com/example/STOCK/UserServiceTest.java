package com.example.STOCK;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testBCrypt() {
        String raw = "Test1234!";
        String hashed = passwordEncoder.encode(raw);

        System.out.println("ハッシュ値: " + hashed);
        assertTrue(passwordEncoder.matches(raw, hashed));
        assertFalse(passwordEncoder.matches("wrong", hashed));
    }
}