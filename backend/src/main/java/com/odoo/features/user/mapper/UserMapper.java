package com.odoo.features.user.mapper;

import com.odoo.features.user.dto.response.UserResponse;
import com.odoo.features.user.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName().name())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}