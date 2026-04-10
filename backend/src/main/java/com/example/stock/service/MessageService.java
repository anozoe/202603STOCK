package com.example.stock.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageId, Object... args) {
        return messageSource.getMessage(messageId, args, Locale.JAPAN);
    }
}