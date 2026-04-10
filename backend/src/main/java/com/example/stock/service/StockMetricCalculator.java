package com.example.stock.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StockMetricCalculator {

    public BigDecimal calculatePer(BigDecimal marketCap, BigDecimal netIncomeTtm) {
        if (marketCap == null || netIncomeTtm == null || BigDecimal.ZERO.compareTo(netIncomeTtm) == 0) {
            return null;
        }
        return marketCap.divide(netIncomeTtm, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateRoe(BigDecimal netIncomeTtm, BigDecimal equityTtm) {
        if (netIncomeTtm == null || equityTtm == null || BigDecimal.ZERO.compareTo(equityTtm) == 0) {
            return null;
        }
        return netIncomeTtm
                .divide(equityTtm, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculateDividendYield(BigDecimal dividendPerShareTtm, BigDecimal currentPrice) {
        if (dividendPerShareTtm == null || currentPrice == null || BigDecimal.ZERO.compareTo(currentPrice) == 0) {
            return null;
        }
        return dividendPerShareTtm
                .divide(currentPrice, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}