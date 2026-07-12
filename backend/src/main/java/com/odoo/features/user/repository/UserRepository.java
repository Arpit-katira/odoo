package com.odoo.features.user.repository;

import com.odoo.features.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u
            FROM User u
            JOIN FETCH u.role
            """)
    List<User> findAllWithRole();

    @Query("""
            SELECT u
            FROM User u
            JOIN FETCH u.role
            WHERE u.id = :id
            """)
    Optional<User> findByIdWithRole(Long id);
}