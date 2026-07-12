package com.odoo.features.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;

    private String phoneNumber;

    @NotNull
    private Long roleId;

    private Boolean isActive;
}