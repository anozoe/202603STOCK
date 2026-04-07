package com.example.stock.service;

import com.example.stock.dto.*;
import com.example.stock.entity.Stock;
import com.example.stock.entity.User;
import com.example.stock.entity.UserFavorite;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.StockRepository;
import com.example.stock.repository.UserFavoriteRepository;
import com.example.stock.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    private static final Long LOGIN_USER_ID = 1L;
    private static final long MAX_FAVORITE_COUNT = 20L;

    private final StockRepository stockRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;

    public StockService(
            StockRepository stockRepository,
            UserFavoriteRepository userFavoriteRepository,
            UserRepository userRepository
    ) {
        this.stockRepository = stockRepository;
        this.userFavoriteRepository = userFavoriteRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public StockListResponse getStocks(String keyword, int page, int size) {
        Page<Stock> result;

        if (keyword == null || keyword.isBlank()) {
            result = stockRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
        } else {
            String trimmed = keyword.trim();
            result = stockRepository
                    .findByTickerCodeContainingIgnoreCaseOrStockNameContainingIgnoreCaseOrderByIdAsc(
                            trimmed, trimmed, PageRequest.of(page, size)
                    );
        }

        long currentFavoriteCount = userFavoriteRepository.countByUserId(LOGIN_USER_ID);

        List<StockListItemResponse> items = result.getContent().stream()
                .map(stock -> new StockListItemResponse(
                        stock.getTickerCode(),
                        stock.getStockName(),
                        stock.getMarket(),
                        stock.getCurrentPrice(),
                        stock.getPriceChange(),
                        stock.getChangeRate(),
                        stock.getMarketCap(),
                        userFavoriteRepository.existsByUserIdAndStockId(LOGIN_USER_ID, stock.getId())
                ))
                .toList();

        return new StockListResponse(
                result.getTotalElements(),
                currentFavoriteCount,
                MAX_FAVORITE_COUNT,
                items
        );
    }

    @Transactional(readOnly = true)
    public StockDetailResponse getStockDetail(String tickerCode) {
        Stock stock = stockRepository.findByTickerCode(tickerCode)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        StockOverviewResponse overview = new StockOverviewResponse(
                decimal(180.10),
                decimal(182.40),
                decimal(179.30),
                decimal(181.80),
                decimal(24.5),
                decimal(6.2),
                decimal(18.3),
                decimal(0.52)
        );

        return new StockDetailResponse(
                stock.getTickerCode(),
                stock.getStockName(),
                stock.getMarket(),
                stock.getCurrentPrice(),
                stock.getPriceChange(),
                stock.getFetchedAt() == null ? "" : stock.getFetchedAt().toString(),
                overview,
                createChartData(7, stock.getCurrentPrice()),
                createChartData(30, stock.getCurrentPrice())
        );
    }

    @Transactional
    public FavoriteToggleResponse toggleFavorite(String tickerCode) {
        Stock stock = stockRepository.findByTickerCode(tickerCode)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        boolean exists = userFavoriteRepository.existsByUserIdAndStockId(LOGIN_USER_ID, stock.getId());

        if (exists) {
            userFavoriteRepository.deleteByUserIdAndStockId(LOGIN_USER_ID, stock.getId());
            return new FavoriteToggleResponse(false);
        }

        User user = userRepository.findById(LOGIN_USER_ID)
                .orElseThrow(() -> new BusinessException("E010", "ユーザ"));

        UserFavorite favorite = new UserFavorite();
        favorite.setUser(user);
        favorite.setStock(stock);
        favorite.setCreatedAt(LocalDateTime.now());
        favorite.setCreatedBy("system");
        userFavoriteRepository.save(favorite);

        return new FavoriteToggleResponse(true);
    }

    private List<StockChartPointResponse> createChartData(int days, BigDecimal base) {
        List<StockChartPointResponse> list = new ArrayList<>();
        BigDecimal current = base == null ? decimal(180) : base;

        for (int i = days; i >= 1; i--) {
            BigDecimal open = current.subtract(decimal(i * 0.3));
            BigDecimal high = open.add(decimal(2.1));
            BigDecimal low = open.subtract(decimal(1.8));
            BigDecimal close = open.add(decimal(0.9));
            BigDecimal ma5 = open.add(decimal(0.4));

            list.add(new StockChartPointResponse(
                    LocalDate.now().minusDays(i).toString(),
                    open, high, low, close, ma5
            ));
        }
        return list;
    }

    private BigDecimal decimal(double value) {
        return BigDecimal.valueOf(value);
    }
}