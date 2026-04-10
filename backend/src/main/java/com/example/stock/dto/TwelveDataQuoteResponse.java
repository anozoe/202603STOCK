package com.example.stock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwelveDataQuoteResponse {
    private String symbol;
    private String name;
    private String exchange;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private String change;

    @JsonProperty("percent_change")
    private String percentChange;

    @JsonProperty("is_market_open")
    private Boolean isMarketOpen;

    private String datetime;
}