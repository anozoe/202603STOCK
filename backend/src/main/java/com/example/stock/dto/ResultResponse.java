package com.example.stock.dto;

public class ResultResponse {
    private String resultCode;

    public ResultResponse(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCode() { return resultCode; }
}