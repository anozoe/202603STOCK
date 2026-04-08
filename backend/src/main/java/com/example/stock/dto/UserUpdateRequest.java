package com.example.stock.dto;

import com.example.stock.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotBlank
    @Size(max = ValidationConstants.USER_NAME_MAX_LENGTH)
    @Pattern(regexp = ValidationConstants.FULL_WIDTH_USER_NAME_REGEX)
    private String userName;

    @NotBlank
    @Email
    @Size(max = ValidationConstants.EMAIL_MAX_LENGTH)
    private String email;
}