package com.example.stock.repository;

import com.example.stock.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Page<Stock> findByTickerCodeContainingIgnoreCaseOrStockNameContainingIgnoreCaseOrderByIdAsc(
            String tickerCode,
            String stockName,
            Pageable pageable
    );

    Page<Stock> findAllByOrderByIdAsc(Pageable pageable);

    Optional<Stock> findByTickerCode(String tickerCode);

    Page<Stock> findAllByOrderByDisplayOrderAscIdAsc(Pageable pageable);

    long countBy();

    Optional<Stock> findTopByOrderByDisplayOrderDesc();

    boolean existsByTickerCodeAndIdNot(String tickerCode, Long id);

    List<Stock> findByIdIn(List<Long> ids);
}