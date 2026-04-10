package com.example.stock.repository;

import com.example.stock.entity.UserFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {

    Page<UserFavorite> findByUserIdOrderByStockIdAsc(Long userId, Pageable pageable);

    boolean existsByUserIdAndStockId(Long userId, Long stockId);

    void deleteByUserIdAndStockId(Long userId, Long stockId);

    int countByUserId(Long userId);

    void deleteByStockId(Long stockId);
}