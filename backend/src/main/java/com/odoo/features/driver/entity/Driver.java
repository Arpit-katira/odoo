package com.odoo.features.driver.entity;

import com.odoo.entities.Trip; // Aryan ke folder se Trip
import com.odoo.entities.User; // Aryan ke folder se User
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private LicenseCategory licenseCategory;

    private LocalDate licenseExpiry;

    private Double safetyScore;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToMany(mappedBy = "driver")
    private List<Trip> trips = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}