package com.example.stock.service;

import com.example.stock.entity.User;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final HttpServletRequest request;
    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        String headerValue = request.getHeader("X-USER-ID");

        if (headerValue == null || headerValue.isBlank()) {
            throw new BusinessException("E010", "ユーザ");
        }

        try {
            return Long.valueOf(headerValue);
        } catch (NumberFormatException e) {
            throw new BusinessException("E010", "ユーザ");
        }
    }

    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("E010", "ユーザ"));
    }
}