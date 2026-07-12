package com.odoo.features.auth.controller;

import com.odoo.common.response.ApiResponse;
import com.odoo.features.auth.dto.request.LoginRequest;
import com.odoo.features.auth.dto.request.RegisterRequest;
import com.odoo.features.auth.dto.response.LoginResponse;
import com.odoo.features.auth.service.AuthService;
import com.odoo.features.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(
            @RequestBody RegisterRequest request
    ) {

        return ApiResponse.success(
                authService.register(request),
                "User registered successfully."
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {

        return ApiResponse.success(
                authService.login(request),
                "Login successful."
        );
    }
}