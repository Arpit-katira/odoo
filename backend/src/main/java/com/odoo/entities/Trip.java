package com.odoo.entities;


import com.odoo.entities.enums.DriverStatus;
import com.odoo.entities.enums.TripStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tripNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    private String source;

    private String destination;

    private Double cargoWeight;

    private Double plannedDistance;

    private Double startOdometer;

    private Double endOdometer;

    private Double revenue;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    private LocalDateTime dispatchedAt;

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "trip")
    private List<FuelLog> fuelLogs = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<Expense> expenses = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
