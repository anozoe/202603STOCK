package com.example.stock.service;

import com.example.stock.constants.BusinessConstants;
import com.example.stock.constants.RoleCode;
import com.example.stock.dto.UserInfoResponse;
import com.example.stock.dto.UserLoginRequest;
import com.example.stock.dto.UserRegisterRequest;
import com.example.stock.dto.UserUpdateRequest;
import com.example.stock.entity.User;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo() {
        User user = getLoginUser();
        return toUserInfoResponse(user);
    }

    @Transactional
    public UserInfoResponse updateMyInfo(UserUpdateRequest request) {
        User user = getLoginUser();

        String normalizedUserName = request.getUserName().replaceAll("[\\s　]+", "");
        String trimmedEmail = request.getEmail().trim();

        userRepository.findByEmailAndIdNot(trimmedEmail, user.getId())
                .ifPresent(u -> {
                    throw new BusinessException("E005", "メールアドレス");
                });

        user.setName(normalizedUserName);
        user.setEmail(trimmedEmail);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy("system");

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new BusinessException("E006");
        }

        return toUserInfoResponse(user);
    }

    private User getLoginUser() {
        return userRepository.findById(BusinessConstants.LOGIN_USER_ID)
                .orElseThrow(() -> new BusinessException("E010", "ユーザ"));
    }

    private UserInfoResponse toUserInfoResponse(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null && user.getRole() == RoleCode.ADMIN ? "管理者" : "一般ユーザ",
                user.getUpdatedAt() == null ? null : user.getUpdatedAt().toString()
        );
    }

    @Transactional
    public void register(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail())
            .ifPresent(u -> { throw new BusinessException("E005", "メールアドレス"); });

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("system");
        userRepository.save(user);
    }

    public User login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BusinessException("E010", "ユーザ"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("E002", "パスワード");
        }
        return user;
    }
}