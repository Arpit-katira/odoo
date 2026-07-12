package com.odoo.config;

import com.odoo.entities.enums.RoleName;
import com.odoo.features.user.entity.Role;
import com.odoo.features.user.entity.User;
import com.odoo.features.user.repository.RoleRepository;
import com.odoo.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            Map<Long, RoleName> roles = Map.of(
                    1L, RoleName.ADMIN,
                    2L, RoleName.DISPATCHER,
                    3L, RoleName.SAFETY_OFFICER,
                    4L, RoleName.FINANCIAL_ANALYST
            );

            roles.forEach((id, name) -> {
                if (!roleRepository.existsById(id)) {
                    Role role = new Role();
                    role.setId(id);
                    role.setName(name);
                    roleRepository.save(role);
                }
            });

            String adminEmail = "admin@transitops.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                Role adminRole = roleRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

                User admin = new User();
                admin.setFullName("Admin User");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setPhoneNumber("9000000001");
                admin.setRole(adminRole);
                admin.setIsActive(true);

                userRepository.save(admin);
            }
        };
    }
}
