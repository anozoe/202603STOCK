package com.example.stock.dto;

import com.example.stock.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotBlank(message = "E001")
    @Size(max = ValidationConstants.USER_NAME_MAX_LENGTH, message = "E003")
    @Pattern(
            regexp = ValidationConstants.FULL_WIDTH_USER_NAME_REGEX,
            message = "E002"
    )
    private String userName;

    @NotBlank(message = "E001")
    @Email(message = "E002")
    @Size(max = ValidationConstants.EMAIL_MAX_LENGTH, message = "E003")
    private String email;
}