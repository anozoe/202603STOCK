package com.example.stock.service;

import com.example.stock.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FmpClient {

    @Value("${app.fmp.base-url}")
    private String baseUrl;

    @Value("${app.fmp.api-key}")
    private String apiKey;

    public FmpMarketCapResponse fetchMarketCap(String symbol) {
        return first("/stable/market-capitalization", symbol, FmpMarketCapResponse[].class);
    }

    public FmpIncomeStatementTtmResponse fetchIncomeStatementTtm(String symbol) {
        return first("/stable/income-statement-ttm", symbol, FmpIncomeStatementTtmResponse[].class);
    }

    public FmpBalanceSheetTtmResponse fetchBalanceSheetTtm(String symbol) {
        return first("/stable/balance-sheet-statement-ttm", symbol, FmpBalanceSheetTtmResponse[].class);
    }

    public FmpKeyMetricsTtmResponse fetchKeyMetricsTtm(String symbol) {
        return first("/stable/key-metrics-ttm", symbol, FmpKeyMetricsTtmResponse[].class);
    }

    public FmpRatiosTtmResponse fetchRatiosTtm(String symbol) {
        return first("/stable/ratios-ttm", symbol, FmpRatiosTtmResponse[].class);
    }

    private <T> T first(String path, String symbol, Class<T[]> clazz) {
        RestClient client = RestClient.create(baseUrl);

        T[] response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(clazz);

        List<T> list = response == null ? List.of() : Arrays.asList(response);
        return list.isEmpty() ? null : list.get(0);
    }
}