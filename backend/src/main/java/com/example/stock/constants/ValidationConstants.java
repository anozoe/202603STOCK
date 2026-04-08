package com.example.stock.constants;

public final class ValidationConstants {

    private ValidationConstants() {
    }

    public static final int USER_NAME_MAX_LENGTH = 30;
    public static final int EMAIL_MAX_LENGTH = 50;

    public static final String FULL_WIDTH_USER_NAME_REGEX = "^[^\\x00-\\x7F]+$";
}