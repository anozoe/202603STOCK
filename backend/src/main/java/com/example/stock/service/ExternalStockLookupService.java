package com.example.stock.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalStockLookupService {

    @Value("${stock.validation.enabled:false}")
    private boolean validationEnabled;

    @Value("${stock.validation.url:}")
    private String validationUrl;

    private final RestClient restClient = RestClient.create();

    public boolean existsTicker(String tickerCode) {
        if (tickerCode == null || tickerCode.isBlank()) {
            return false;
        }

        if (!validationEnabled || validationUrl == null || validationUrl.isBlank()) {
            return true;
        }

        try {
            String body = restClient.get()
                    .uri(validationUrl, tickerCode)
                    .retrieve()
                    .body(String.class);

            return body != null && !body.isBlank();
        } catch (Exception e) {
            return false;
        }
    }
}