package com.odoo.features.fuel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FuelResponseDTO {

    private Long id;
    private Long tripId;
    private Double liters;
    private Double cost;
    private Double odometerReading;
    private LocalDateTime loggedAt;
}