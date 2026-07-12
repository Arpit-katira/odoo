package com.odoo.features.maintenance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odoo.features.vehicle.entity.Vehicle; // 🔥 Naya aur Sahi Import 🔥
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "vehicle_id")
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