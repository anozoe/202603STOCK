package com.example.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String messageId;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String messageId, String message, T data) {
        return new ApiResponse<>(messageId, message, data);
    }

    public static <T> ApiResponse<T> error(String messageId, String message) {
        return new ApiResponse<>(messageId, message, null);
    }
}

//messageIdは機械可読なエラーコードや成功コードを表す文字列で、messageは人間が理解しやすい説明文を表す文字列です。dataは任意の型Tのデータを格納するためのフィールドです。successメソッドは成功時のレスポンスを生成し、errorメソッドはエラー時のレスポンスを生成します。