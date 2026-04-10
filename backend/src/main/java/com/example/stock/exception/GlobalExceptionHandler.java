package com.example.stock.exception;

import com.example.stock.constants.ValidationConstants;
import com.example.stock.dto.ApiResponse;
import com.example.stock.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageService messageService;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest().body(
                ApiResponse.error(
                        e.getMessageId(),
                        messageService.getMessage(e.getMessageId(), e.getArgs())
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

        if (fieldError == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("E013", messageService.getMessage("E013", "入力チェック"))
            );
        }

        String fieldName = switch (fieldError.getField()) {
            case "userName" -> "ユーザ名";
            case "email" -> "メールアドレス";
            default -> fieldError.getField();
        };

        String code = fieldError.getCode();
        if ("NotBlank".equals(code)) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("E001", messageService.getMessage("E001", fieldName))
            );
        }
        if ("Email".equals(code) || "Pattern".equals(code)) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("E002", messageService.getMessage("E002", fieldName))
            );
        }
        if ("Size".equals(code)) {
            String max = "userName".equals(fieldError.getField())
                    ? String.valueOf(ValidationConstants.USER_NAME_MAX_LENGTH)
                    : String.valueOf(ValidationConstants.EMAIL_MAX_LENGTH);

            return ResponseEntity.badRequest().body(
                    ApiResponse.error("E003", messageService.getMessage("E003", fieldName, max))
            );
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.error("E013", messageService.getMessage("E013", "入力チェック"))
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error("E013", messageService.getMessage("E013", "処理"))
        );
    }
}