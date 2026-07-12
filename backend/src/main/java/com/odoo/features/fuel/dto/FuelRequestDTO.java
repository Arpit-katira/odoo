package com.odoo.features.fuel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FuelRequestDTO {
    @NotNull(message = "Trip ID cannot be null")
    private Long tripId;

    @NotNull
    @DecimalMin(value = "0.1", message = "Liters must be greater than 0")
    private Double liters;

    @NotNull
    @DecimalMin(value = "0.0", message = "Cost cannot be negative")
    private Double cost;

    private Double odometerReading;
}