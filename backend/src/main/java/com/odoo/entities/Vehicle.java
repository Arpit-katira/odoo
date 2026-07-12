package com.odoo.entities;


import com.odoo.entities.enums.VehicleStatus;
import com.odoo.entities.enums.VehicleType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    private String model;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private Double maxLoadCapacity;

    private Double acquisitionCost;

    private Double odometerReading;

    private String region;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @OneToMany(mappedBy = "vehicle")
    private List<Trip> trips = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle")
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}