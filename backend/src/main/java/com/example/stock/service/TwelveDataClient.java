package com.example.stock.service;

import com.example.stock.dto.TwelveDataQuoteResponse;
import com.example.stock.dto.TwelveDataTimeSeriesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TwelveDataClient {

    @Value("${app.twelvedata.base-url}")
    private String baseUrl;

    @Value("${app.twelvedata.api-key}")
    private String apiKey;

    public TwelveDataQuoteResponse fetchQuote(String symbol) {
        RestClient client = RestClient.create(baseUrl);

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(TwelveDataQuoteResponse.class);
    }

    public TwelveDataTimeSeriesResponse fetchDailyTimeSeries(String symbol, int outputSize) {
        RestClient client = RestClient.create(baseUrl);

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/time_series")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", "1day")
                        .queryParam("outputsize", outputSize)
                        .queryParam("order", "DESC")
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(TwelveDataTimeSeriesResponse.class);
    }
}