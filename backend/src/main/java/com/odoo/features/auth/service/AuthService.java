package com.odoo.features.auth.service;

import com.odoo.common.exception.ConflictException;
import com.odoo.common.exception.ResourceNotFoundException;
import com.odoo.features.auth.dto.request.LoginRequest;
import com.odoo.features.auth.dto.request.RegisterRequest;
import com.odoo.features.auth.dto.response.LoginResponse;
import com.odoo.features.auth.security.JwtService;
import com.odoo.features.user.dto.response.UserResponse;
import com.odoo.features.user.entity.Role;
import com.odoo.features.user.entity.User;
import com.odoo.features.user.mapper.UserMapper;
import com.odoo.features.user.repository.RoleRepository;
import com.odoo.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists.");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found."));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .isActive(true)
                .build();

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .user(UserMapper.toResponse(user))
                .build();
    }
}