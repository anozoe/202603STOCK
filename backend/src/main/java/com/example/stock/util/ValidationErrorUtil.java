package com.example.stock.util;

import com.example.stock.constants.ValidationConstants;
import com.example.stock.dto.ValidationErrorResponse;
import com.example.stock.service.MessageService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public final class ValidationErrorUtil {

    private ValidationErrorUtil() {
    }

    public static ValidationErrorResponse getFirstError(
            BindingResult bindingResult,
            MessageService messageService
    ) {
        if (!bindingResult.hasErrors()) {
            return null;
        }

        FieldError fieldError = bindingResult.getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        if (fieldError == null) {
            return new ValidationErrorResponse(
                    "E013",
                    messageService.getMessage("E013", "入力チェック")
            );
        }

        String messageId = fieldError.getDefaultMessage();
        String fieldName = resolveFieldName(fieldError.getField());

        if ("E001".equals(messageId)) {
            return new ValidationErrorResponse(
                    "E001",
                    messageService.getMessage("E001", fieldName)
            );
        }

        if ("E002".equals(messageId)) {
            return new ValidationErrorResponse(
                    "E002",
                    messageService.getMessage("E002", fieldName)
            );
        }

        if ("E003".equals(messageId)) {
            return new ValidationErrorResponse(
                    "E003",
                    messageService.getMessage("E003", fieldName, resolveMaxLength(fieldError.getField()))
            );
        }

        if ("E011".equals(messageId)) {
            return new ValidationErrorResponse(
                    "E011",
                    messageService.getMessage("E011", fieldName)
            );
        }

        return new ValidationErrorResponse(
                "E013",
                messageService.getMessage("E013", "入力チェック")
        );
    }

    private static String resolveFieldName(String field) {
        return switch (field) {
            case "userName" -> "ユーザ名";
            case "email" -> "メールアドレス";
            case "tickerCode" -> "銘柄コード";
            case "stockName" -> "銘柄名";
            case "market" -> "市場";
            case "currentPrice" -> "現在値";
            case "displayOrder" -> "表示順";
            case "stockIds" -> "並び順";
            default -> field;
        };
    }

    private static String resolveMaxLength(String field) {
        return switch (field) {
            case "userName" -> String.valueOf(ValidationConstants.USER_NAME_MAX_LENGTH);
            case "email" -> String.valueOf(ValidationConstants.EMAIL_MAX_LENGTH);
            case "tickerCode" -> "20";
            case "stockName" -> "100";
            default -> "";
        };
    }
}