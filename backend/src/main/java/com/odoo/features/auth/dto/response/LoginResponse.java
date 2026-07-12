package com.odoo.features.auth.dto.response;

import com.odoo.features.user.dto.response.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;

    private UserResponse user;
}