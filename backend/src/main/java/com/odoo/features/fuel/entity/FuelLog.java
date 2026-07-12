package com.odoo.features.fuel.entity;

import com.odoo.features.trip.entity.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private Double liters;

    @Column(nullable = false)
    private Double cost;

    @Column(name = "odometer_reading", nullable = false)
    private Double odometerReading;

    @Column(name = "logged_at")
    private LocalDateTime loggedAt;
}