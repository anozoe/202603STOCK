package com.example.stock.exception;

public class BusinessException extends RuntimeException {

    private final String messageId;
    private final Object[] args;

    public BusinessException(String messageId, Object... args) {
        super(messageId);
        this.messageId = messageId;
        this.args = args;
    }

    public String getMessageId() { return messageId; }
    public Object[] getArgs() { return args; }
}