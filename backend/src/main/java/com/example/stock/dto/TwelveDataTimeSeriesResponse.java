package com.example.stock.dto;

import lombok.Data;

import java.util.List;

@Data
public class TwelveDataTimeSeriesResponse {

    private Meta meta;
    private List<Value> values;
    private String status;

    @Data
    public static class Meta {
        private String symbol;
        private String interval;
        private String exchange;
        private String type;
    }

    @Data
    public static class Value {
        private String datetime;
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;
    }
}