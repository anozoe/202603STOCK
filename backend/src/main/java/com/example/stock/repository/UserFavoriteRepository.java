package com.example.stock.repository;

import com.example.stock.entity.UserFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {

    Page<UserFavorite> findByUserIdOrderByStockIdAsc(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    Optional<UserFavorite> findByUserIdAndStockTickerCode(Long userId, String tickerCode);
}