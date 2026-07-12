package com.odoo.entities;


import com.odoo.features.trip.entity.Trip;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_logs")
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private Double liters;

    private Double cost;

    private Double odometerReading;

    private LocalDateTime loggedAt;
}