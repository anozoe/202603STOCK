package com.example.stock.dto;

public class ApiResponse<T> {

    private String messageId;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(String messageId, String message, T data) {
        this.messageId = messageId;
        this.message = message;
        this.data = data;
    }

    public String getMessageId() { return messageId; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public void setMessageId(String messageId) { this.messageId = messageId; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
}