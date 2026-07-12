package com.odoo.features.user.controller;

import com.odoo.features.user.dto.request.CreateUserRequest;
import com.odoo.features.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.odoo.common.response.ApiResponse;
import com.odoo.features.user.dto.request.ChangePasswordRequest;
import com.odoo.features.user.dto.request.UpdateUserRequest;
import com.odoo.features.user.dto.response.UserResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(
            @RequestBody CreateUserRequest request
    ) {
        return ApiResponse.success(
                userService.createUser(request),
                "User created successfully."
        );
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.success(
                userService.getAllUsers(),
                "Users fetched successfully."
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(
            @PathVariable Long id
    ) {
        return ApiResponse.success(
                userService.getUserById(id),
                "User fetched successfully."
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return ApiResponse.success(
                userService.updateUser(id, request),
                "User updated successfully."
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);

        return ApiResponse.success(
                null,
                "User deleted successfully."
        );
    }

    @PatchMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(id, request);

        return ApiResponse.success(
                null,
                "Password changed successfully."
        );
    }
}