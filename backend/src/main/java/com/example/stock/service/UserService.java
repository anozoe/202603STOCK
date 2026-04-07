package com.example.stock.service;

import com.example.stock.dto.*;
import com.example.stock.entity.User;
import com.example.stock.entity.UserFavorite;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.UserFavoriteRepository;
import com.example.stock.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Long LOGIN_USER_ID = 1L;
    private static final long MAX_FAVORITES = 20L;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern FULL_WIDTH_PATTERN =
            Pattern.compile("^[^\\x00-\\x7F]+$");

    private final UserRepository userRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    public UserService(UserRepository userRepository, UserFavoriteRepository userFavoriteRepository) {
        this.userRepository = userRepository;
        this.userFavoriteRepository = userFavoriteRepository;
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo() {
        User user = getLoginUser();
        return new UserInfoResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null && user.getRole() == 2 ? "管理者" : "一般ユーザ"
        );
    }

    @Transactional
    public UserUpdateResponse updateMyInfo(UserUpdateRequest request) {
        String userName = normalizeSpaces(request.getUserName());
        String email = trimToEmpty(request.getEmail());

        validateUserName(userName);
        validateEmail(email);

        User user = getLoginUser();

        userRepository.findByEmailAndIdNot(email, user.getId())
                .ifPresent(u -> { throw new BusinessException("E005", "メールアドレス"); });

        user.setName(userName);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy("system");

        try {
          userRepository.save(user);
        } catch (Exception e) {
          throw new BusinessException("E006");
        }

        return new UserUpdateResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null && user.getRole() == 2 ? "管理者" : "一般ユーザ",
                LocalDateTime.now().toString()
        );
    }

    @Transactional(readOnly = true)
    public FavoriteStockListResponse getMyFavorites(int page, int size) {
        Page<UserFavorite> result = userFavoriteRepository.findByUserIdOrderByStockIdAsc(
                LOGIN_USER_ID,
                PageRequest.of(page, size)
        );

        long currentCount = userFavoriteRepository.countByUserId(LOGIN_USER_ID);

        List<FavoriteStockItemResponse> items = result.getContent().stream()
                .map(UserFavorite::getStock)
                .map(stock -> new FavoriteStockItemResponse(
                        stock.getTickerCode(),
                        stock.getStockName(),
                        stock.getMarket(),
                        stock.getCurrentPrice(),
                        stock.getPriceChange(),
                        stock.getChangeRate(),
                        stock.getMarketCap(),
                        true
                ))
                .toList();

        return new FavoriteStockListResponse(
                result.getTotalElements(),
                currentCount,
                MAX_FAVORITES,
                items
        );
    }

    @Transactional
    public ResultResponse removeFavorite(String tickerCode) {
        UserFavorite favorite = userFavoriteRepository
                .findByUserIdAndStockTickerCode(LOGIN_USER_ID, tickerCode)
                .orElseThrow(() -> new BusinessException("E010", "お気に入り銘柄"));

        userFavoriteRepository.delete(favorite);
        return new ResultResponse("SUCCESS");
    }

    private User getLoginUser() {
        return userRepository.findById(LOGIN_USER_ID)
                .orElseThrow(() -> new BusinessException("E010", "ユーザ"));
    }

    private void validateUserName(String userName) {
        if (userName.isBlank()) throw new BusinessException("E001", "ユーザ名");
        if (userName.length() > 30) throw new BusinessException("E003", "ユーザ名", "30");
        if (!FULL_WIDTH_PATTERN.matcher(userName).matches()) throw new BusinessException("E002", "ユーザ名");
    }

    private void validateEmail(String email) {
        if (email.isBlank()) throw new BusinessException("E001", "メールアドレス");
        if (email.length() > 50) throw new BusinessException("E003", "メールアドレス", "50");
        if (!EMAIL_PATTERN.matcher(email).matches()) throw new BusinessException("E002", "メールアドレス");
    }

    private String normalizeSpaces(String value) {
        if (value == null) return "";
        return value.replaceAll("[\\s　]+", "");
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}