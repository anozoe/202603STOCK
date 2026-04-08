package com.example.stock.service;

import com.example.stock.constants.BusinessConstants;
import com.example.stock.constants.RoleCode;
import com.example.stock.dto.AdminStockItemResponse;
import com.example.stock.dto.AdminStockListResponse;
import com.example.stock.dto.AdminStockReorderRequest;
import com.example.stock.dto.AdminStockUpsertRequest;
import com.example.stock.dto.AdminUserItemResponse;
import com.example.stock.dto.AdminUserListResponse;
import com.example.stock.entity.Stock;
import com.example.stock.entity.User;
import com.example.stock.exception.BusinessException;
import com.example.stock.repository.StockRepository;
import com.example.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final ExternalStockLookupService externalStockLookupService;

    @Transactional(readOnly = true)
    public AdminStockListResponse getStocks(int page, int size) {
        var result = stockRepository.findAllByOrderByDisplayOrderAscIdAsc(PageRequest.of(page, size));

        return new AdminStockListResponse(
                stockRepository.countBy(),
                result.getContent().stream()
                        .map(s -> new AdminStockItemResponse(
                                s.getId(),
                                s.getTickerCode(),
                                s.getStockName(),
                                s.getMarket(),
                                s.getCurrentPrice(),
                                s.getDisplayOrder()
                        ))
                        .toList()
        );
    }

    @Transactional
    public void createStock(AdminStockUpsertRequest req) {
        if (stockRepository.countBy() >= BusinessConstants.MAX_ADMIN_STOCK_COUNT) {
            throw new BusinessException("E012", "銘柄", "登録");
        }

        validateStockRequest(req, null);

        Stock stock = new Stock();
        setStock(stock, req);

        if (stock.getPriceChange() == null) {
            stock.setPriceChange(BigDecimal.ZERO);
        }
        if (stock.getChangeRate() == null) {
            stock.setChangeRate(BigDecimal.ZERO);
        }
        if (stock.getMarketCap() == null) {
            stock.setMarketCap(0L);
        }
        if (stock.getFetchedAt() == null) {
            stock.setFetchedAt(LocalDateTime.now());
        }

        stockRepository.save(stock);
    }

    @Transactional
    public void updateStock(AdminStockUpsertRequest req) {
        if (req.getId() == null) {
            throw new BusinessException("E001", "ID");
        }

        Stock stock = stockRepository.findById(req.getId())
                .orElseThrow(() -> new BusinessException("E010", "銘柄"));

        validateStockRequest(req, req.getId());

        setStock(stock, req);

        if (stock.getPriceChange() == null) {
            stock.setPriceChange(BigDecimal.ZERO);
        }
        if (stock.getChangeRate() == null) {
            stock.setChangeRate(BigDecimal.ZERO);
        }
        if (stock.getMarketCap() == null) {
            stock.setMarketCap(0L);
        }
        if (stock.getFetchedAt() == null) {
            stock.setFetchedAt(LocalDateTime.now());
        }

        stockRepository.save(stock);
    }

    @Transactional
    public void reorder(AdminStockReorderRequest req) {
        if (req.getStockIds() == null || req.getStockIds().isEmpty()) {
            throw new BusinessException("E001", "並び順");
        }

        List<Stock> stocks = stockRepository.findByIdIn(req.getStockIds()).stream()
                .sorted(Comparator.comparingInt(s -> req.getStockIds().indexOf(s.getId())))
                .toList();

        int order = 1;
        for (Stock stock : stocks) {
            stock.setDisplayOrder(order++);
        }

        stockRepository.saveAll(stocks);
    }

    @Transactional(readOnly = true)
    public AdminUserListResponse getUsers(int page, int size) {
        var result = userRepository.findByDeletedAtIsNullOrderByIdAsc(PageRequest.of(page, size));

        return new AdminUserListResponse(
                userRepository.countByDeletedAtIsNull(),
                BusinessConstants.MAX_ADMIN_USER_DISPLAY_COUNT,
                result.getContent().stream()
                        .map(this::toAdminUserItemResponse)
                        .toList()
        );
    }

    @Transactional
    public void logicalDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("E010", "ユーザ"));

        user.setDeletedAt(LocalDateTime.now());
        user.setDeletedBy("admin");

        userRepository.save(user);
    }

    private void setStock(Stock stock, AdminStockUpsertRequest req) {
        stock.setTickerCode(req.getTickerCode().trim());
        stock.setStockName(req.getStockName().trim());
        stock.setMarket(req.getMarket());
        stock.setCurrentPrice(req.getCurrentPrice());
        stock.setPriceChange(req.getPriceChange());
        stock.setChangeRate(req.getChangeRate());
        stock.setMarketCap(req.getMarketCap());
        stock.setDisplayOrder(req.getDisplayOrder());
    }

    private void validateStockRequest(AdminStockUpsertRequest req, Long id) {
        String tickerCode = req.getTickerCode() == null ? "" : req.getTickerCode().trim();

        if (id == null) {
            stockRepository.findByTickerCode(tickerCode)
                    .ifPresent(s -> {
                        throw new BusinessException("E005", "銘柄コード");
                    });
        } else if (stockRepository.existsByTickerCodeAndIdNot(tickerCode, id)) {
            throw new BusinessException("E005", "銘柄コード");
        }

        if (!externalStockLookupService.existsTicker(tickerCode)) {
            throw new BusinessException("E002", "銘柄コード");
        }
    }

    private AdminUserItemResponse toAdminUserItemResponse(User user) {
        String roleName = user.getRole() != null && user.getRole() == RoleCode.ADMIN
                ? "管理者"
                : "一般ユーザ";

        return new AdminUserItemResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleName
        );
    }
}