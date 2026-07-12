package com.odoo.features.user.service;

import com.odoo.common.exception.ConflictException;
import com.odoo.common.exception.ResourceNotFoundException;
import com.odoo.features.user.dto.request.ChangePasswordRequest;
import com.odoo.features.user.dto.request.CreateUserRequest;
import com.odoo.features.user.dto.request.UpdateUserRequest;
import com.odoo.features.user.dto.response.UserResponse;
import com.odoo.features.user.entity.Role;
import com.odoo.features.user.entity.User;
import com.odoo.features.user.mapper.UserMapper;
import com.odoo.features.user.repository.RoleRepository;
import com.odoo.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("User with this email already exists.");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())   // TODO: BCrypt
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .isActive(true)
                .build();

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAllWithRole()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findByIdWithRole(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        return UserMapper.toResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findByIdWithRole(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new ConflictException("User with this email already exists.");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(role);
        user.setIsActive(request.getIsActive());

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findByIdWithRole(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        userRepository.delete(user);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        // TODO: Validate old password + BCrypt encode
        user.setPassword(request.getNewPassword());

        userRepository.save(user);
    }
}