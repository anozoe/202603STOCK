package com.example.stock.repository;

import com.example.stock.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("""
        SELECT s
        FROM Stock s
        WHERE (:keyword IS NULL OR :keyword = ''
            OR LOWER(s.tickerCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(s.stockName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        ORDER BY s.id ASC
    """)
    Page<Stock> searchStocks(@Param("keyword") String keyword, Pageable pageable);

    Optional<Stock> findByTickerCode(String tickerCode);

    Page<Stock> findAllByOrderByDisplayOrderAscIdAsc(Pageable pageable);

    long countBy();

    Optional<Stock> findTopByOrderByDisplayOrderDesc();

    List<Stock> findByIdIn(List<Long> ids);

    boolean existsByTickerCodeAndIdNot(String tickerCode, Long id);
}