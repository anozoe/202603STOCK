package com.example.stock.service;

import com.example.stock.dto.*;
import com.example.stock.entity.Stock;
import com.example.stock.entity.User;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.StockRepository;
import com.example.stock.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AdminService {

    private static final long MAX_STOCK_COUNT = 100L;
    private static final long MAX_USER_DISPLAY_COUNT = 100L;

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final ExternalStockLookupService externalStockLookupService;

    public AdminService(
            StockRepository stockRepository,
            UserRepository userRepository,
            ExternalStockLookupService externalStockLookupService
    ) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.externalStockLookupService = externalStockLookupService;
    }

    @Transactional(readOnly = true)
    public AdminStockListResponse getStocks(int page, int size) {
        Page<Stock> result = stockRepository.findAllByOrderByDisplayOrderAscIdAsc(PageRequest.of(page, size));
        long currentCount = stockRepository.countBy();

        List<AdminStockItemResponse> items = result.getContent().stream()
                .map(stock -> new AdminStockItemResponse(
                        stock.getId(),
                        stock.getTickerCode(),
                        stock.getStockName(),
                        stock.getDisplayOrder()
                ))
                .toList();

        return new AdminStockListResponse(
                result.getTotalElements(),
                currentCount,
                MAX_STOCK_COUNT,
                items
        );
    }

    @Transactional
    public void createStock(AdminStockUpsertRequest request) {
        long currentCount = stockRepository.countBy();
        if (currentCount >= MAX_STOCK_COUNT) {
            throw new BusinessException("E012", "銘柄", "登録");
        }

        validateStockRequest(request, null);

        Stock stock = new Stock();
        stock.setTickerCode(request.getTickerCode().trim());
        stock.setStockName(request.getStockName().trim());
        stock.setMarket(1);
        stock.setCurrentPrice(java.math.BigDecimal.ZERO);
        stock.setPriceChange(java.math.BigDecimal.ZERO);
        stock.setChangeRate(java.math.BigDecimal.ZERO);
        stock.setMarketCap(0L);
        stock.setFetchedAt(LocalDateTime.now());

        Integer nextOrder = stockRepository.findTopByOrderByDisplayOrderDesc()
                .map(s -> (s.getDisplayOrder() == null ? 0 : s.getDisplayOrder()) + 1)
                .orElse(1);

        stock.setDisplayOrder(nextOrder);
        stockRepository.save(stock);
    }

    @Transactional
    public void updateStock(Long id, AdminStockUpsertRequest request) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        validateStockRequest(request, id);

        stock.setTickerCode(request.getTickerCode().trim());
        stock.setStockName(request.getStockName().trim());
        stockRepository.save(stock);
    }

    @Transactional
    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        stockRepository.delete(stock);
    }

    @Transactional
    public void reorderStocks(AdminStockReorderRequest request) {
        if (request.getStockIds() == null || request.getStockIds().isEmpty()) {
          throw new BusinessException("E001", "並び順");
        }

        List<Stock> stocks = stockRepository.findByIdIn(request.getStockIds()).stream()
                .sorted(Comparator.comparingInt(s -> request.getStockIds().indexOf(s.getId())))
                .toList();

        for (int i = 0; i < stocks.size(); i += 1) {
            stocks.get(i).setDisplayOrder(i + 1);
        }

        stockRepository.saveAll(stocks);
    }

    @Transactional(readOnly = true)
    public AdminUserListResponse getUsers(int page, int size) {
        Page<User> result = userRepository.findByDeletedAtIsNullOrderByIdAsc(PageRequest.of(page, size));
        long totalCount = userRepository.countByDeletedAtIsNull();

        List<AdminUserItemResponse> items = result.getContent().stream()
                .map(user -> new AdminUserItemResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole() != null && user.getRole() == 2 ? "管理者" : "一般ユーザ"
                ))
                .toList();

        return new AdminUserListResponse(totalCount, MAX_USER_DISPLAY_COUNT, items);
    }

    @Transactional
    public void logicalDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("E010", "ユーザ"));

        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy("admin");
        userRepository.save(user);
    }

    private void validateStockRequest(AdminStockUpsertRequest request, Long id) {
        if (request.getTickerCode() == null || request.getTickerCode().isBlank()) {
            throw new BusinessException("E001", "銘柄コード");
        }
        if (request.getStockName() == null || request.getStockName().isBlank()) {
            throw new BusinessException("E001", "銘柄名");
        }

        String tickerCode = request.getTickerCode().trim();

        if (id == null) {
            stockRepository.findByTickerCode(tickerCode)
                    .ifPresent(s -> { throw new BusinessException("E005", "銘柄コード"); });
        } else if (stockRepository.existsByTickerCodeAndIdNot(tickerCode, id)) {
            throw new BusinessException("E005", "銘柄コード");
        }

        if (!externalStockLookupService.existsTicker(tickerCode)) {
            throw new BusinessException("E002", "銘柄コード");
        }
    }
}