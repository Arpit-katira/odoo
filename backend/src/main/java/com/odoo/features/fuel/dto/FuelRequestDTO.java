package com.odoo.features.fuel.dto;

import lombok.Data;

@Data
public class FuelRequestDTO {

    private Long tripId;
    private Double liters;
    private Double cost;
    private Double odometerReading;
}