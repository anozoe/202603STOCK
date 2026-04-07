package com.example.stock.repository;

import com.example.stock.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Page<Stock> findByTickerCodeContainingIgnoreCaseOrStockNameContainingIgnoreCaseOrderByIdAsc(
            String tickerCode,
            String stockName,
            Pageable pageable
    );

    Page<Stock> findAllByOrderByIdAsc(Pageable pageable);

    Optional<Stock> findByTickerCode(String tickerCode);
}