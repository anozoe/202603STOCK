package com.example.stock.repository;

import com.example.stock.entity.StockPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, Long> {

    void deleteByStockId(Long stockId);

    List<StockPriceHistory> findTop30ByStockIdOrderByPriceDateDesc(Long stockId);

    List<StockPriceHistory> findTop7ByStockIdOrderByPriceDateDesc(Long stockId);
}