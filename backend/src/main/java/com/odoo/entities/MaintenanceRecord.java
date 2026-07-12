package com.odoo.entities;


import com.odoo.entities.enums.MaintenanceStatus;
import com.odoo.features.vehicle.entity.Vehicle;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private String issue;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double cost;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}