package com.example.stock.service;

import com.example.stock.constants.BusinessConstants;
import com.example.stock.dto.FavoriteStockListResponse;
import com.example.stock.dto.FavoriteToggleResponse;
import com.example.stock.dto.StockChartPointResponse;
import com.example.stock.dto.StockDetailResponse;
import com.example.stock.dto.StockListItemResponse;
import com.example.stock.dto.StockListResponse;
import com.example.stock.dto.StockOverviewResponse;
import com.example.stock.entity.Stock;
import com.example.stock.entity.User;
import com.example.stock.entity.UserFavorite;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.StockRepository;
import com.example.stock.repository.UserFavoriteRepository;
import com.example.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class StockService {

    /*詳細画面の概要情報は現状ダミー実装。*/
    private static final BigDecimal DUMMY_OPEN_PRICE = BigDecimal.valueOf(180.10);
    private static final BigDecimal DUMMY_HIGH_PRICE = BigDecimal.valueOf(182.40);
    private static final BigDecimal DUMMY_LOW_PRICE = BigDecimal.valueOf(179.30);
    private static final BigDecimal DUMMY_CLOSE_PRICE = BigDecimal.valueOf(181.80);
    private static final BigDecimal DUMMY_PER = BigDecimal.valueOf(24.5);
    private static final BigDecimal DUMMY_PBR = BigDecimal.valueOf(6.2);
    private static final BigDecimal DUMMY_ROE = BigDecimal.valueOf(18.3);
    private static final BigDecimal DUMMY_DIVIDEND_YIELD = BigDecimal.valueOf(0.52);

    /*チャート情報は現状ダミー生成。*/
    private static final BigDecimal DEFAULT_BASE_PRICE = BigDecimal.valueOf(180);
    private static final BigDecimal DUMMY_OPEN_DIFF_PER_DAY = BigDecimal.valueOf(0.3);
    private static final BigDecimal DUMMY_HIGH_DIFF = BigDecimal.valueOf(2.1);
    private static final BigDecimal DUMMY_LOW_DIFF = BigDecimal.valueOf(1.8);
    private static final BigDecimal DUMMY_CLOSE_DIFF = BigDecimal.valueOf(0.9);
    private static final BigDecimal DUMMY_MA5_DIFF = BigDecimal.valueOf(0.4);

    private final StockRepository stockRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public StockListResponse getStocks(String keyword, int page, int size) {
        Page<Stock> result = stockRepository.searchStocks(
                keyword == null ? "" : keyword.trim(),
                PageRequest.of(page, size)
        );

        int currentFavoriteCount = userFavoriteRepository.countByUserId(BusinessConstants.LOGIN_USER_ID);

        List<StockListItemResponse> items = result.getContent().stream()
                .map(stock -> new StockListItemResponse(
                        stock.getTickerCode(),
                        stock.getStockName(),
                        stock.getMarket(),
                        stock.getCurrentPrice(),
                        stock.getPriceChange(),
                        stock.getChangeRate(),
                        stock.getMarketCap(),
                        userFavoriteRepository.existsByUserIdAndStockId(BusinessConstants.LOGIN_USER_ID, stock.getId())
                ))
                .toList();

        return new StockListResponse(
                Math.toIntExact(result.getTotalElements()),
                page,
                size,
                result.getTotalPages(),
                currentFavoriteCount,
                BusinessConstants.MAX_FAVORITE_COUNT,
                items
        );
    }

    @Transactional(readOnly = true)
    public FavoriteStockListResponse getFavoriteStocks(int page, int size) {
        Page<UserFavorite> result = userFavoriteRepository.findByUserIdOrderByStockIdAsc(
                BusinessConstants.LOGIN_USER_ID,
                PageRequest.of(page, size)
        );

        int currentFavoriteCount = userFavoriteRepository.countByUserId(BusinessConstants.LOGIN_USER_ID);

        List<StockListItemResponse> items = result.getContent().stream()
                .map(UserFavorite::getStock)
                .map(stock -> new StockListItemResponse(
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
                Math.toIntExact(result.getTotalElements()),
                page,
                size,
                result.getTotalPages(),
                currentFavoriteCount,
                BusinessConstants.MAX_FAVORITE_COUNT,
                items
        );
    }

    @Transactional(readOnly = true)
    public StockDetailResponse getStockDetail(String tickerCode) {
        Stock stock = stockRepository.findByTickerCode(tickerCode)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        StockOverviewResponse overview = buildDummyOverview();

        return new StockDetailResponse(
                stock.getTickerCode(),
                stock.getStockName(),
                stock.getMarket(),
                stock.getCurrentPrice(),
                stock.getPriceChange(),
                stock.getFetchedAt(),
                overview,
                createDummyChartData(BusinessConstants.WEEK_CHART_DAYS, stock.getCurrentPrice()),
                createDummyChartData(BusinessConstants.MONTH_CHART_DAYS, stock.getCurrentPrice())
        );
    }

    @Transactional
    public FavoriteToggleResponse addFavorite(String tickerCode) {
        Stock stock = stockRepository.findByTickerCode(tickerCode)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        int currentFavoriteCount = userFavoriteRepository.countByUserId(BusinessConstants.LOGIN_USER_ID);
        if (currentFavoriteCount >= BusinessConstants.MAX_FAVORITE_COUNT) {
            throw new BusinessException("E012", "お気に入り銘柄", "登録");
        }

        boolean exists = userFavoriteRepository.existsByUserIdAndStockId(BusinessConstants.LOGIN_USER_ID, stock.getId());
        if (!exists) {
            User user = userRepository.findById(BusinessConstants.LOGIN_USER_ID)
                    .orElseThrow(() -> new BusinessException("E010", "ユーザ"));

            UserFavorite favorite = new UserFavorite();
            favorite.setUser(user);
            favorite.setStock(stock);
            favorite.setCreatedAt(LocalDateTime.now());
            favorite.setCreatedBy("system");
            userFavoriteRepository.save(favorite);
        }

        return new FavoriteToggleResponse(true);
    }

    @Transactional
    public FavoriteToggleResponse removeFavorite(String tickerCode) {
        Stock stock = stockRepository.findByTickerCode(tickerCode)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        boolean exists = userFavoriteRepository.existsByUserIdAndStockId(BusinessConstants.LOGIN_USER_ID, stock.getId());
        if (exists) {
            userFavoriteRepository.deleteByUserIdAndStockId(BusinessConstants.LOGIN_USER_ID, stock.getId());
        }

        return new FavoriteToggleResponse(false);
    }

    private StockOverviewResponse buildDummyOverview() {
        return new StockOverviewResponse(
                DUMMY_OPEN_PRICE,
                DUMMY_HIGH_PRICE,
                DUMMY_LOW_PRICE,
                DUMMY_CLOSE_PRICE,
                DUMMY_PER,
                DUMMY_PBR,
                DUMMY_ROE,
                DUMMY_DIVIDEND_YIELD
        );
    }

    private List<StockChartPointResponse> createDummyChartData(int days, BigDecimal basePrice) {
        List<StockChartPointResponse> chartPoints = new ArrayList<>();
        BigDecimal currentBasePrice = basePrice == null ? DEFAULT_BASE_PRICE : basePrice;

        for (int i = days; i >= 1; i--) {
            BigDecimal openPrice = currentBasePrice.subtract(
                    DUMMY_OPEN_DIFF_PER_DAY.multiply(BigDecimal.valueOf(i))
            );
            BigDecimal highPrice = openPrice.add(DUMMY_HIGH_DIFF);
            BigDecimal lowPrice = openPrice.subtract(DUMMY_LOW_DIFF);
            BigDecimal closePrice = openPrice.add(DUMMY_CLOSE_DIFF);
            BigDecimal movingAverage5 = openPrice.add(DUMMY_MA5_DIFF);

            chartPoints.add(new StockChartPointResponse(
                    LocalDate.now().minusDays(i),
                    openPrice,
                    highPrice,
                    lowPrice,
                    closePrice,
                    movingAverage5
            ));
        }

        return chartPoints;
    }
}