package com.odoo.features.vehicle.dto.request;

import com.odoo.entities.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateVehicleRequest {

    @NotBlank
    private String registrationNumber;

    @NotBlank
    private String model;

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    private Double maxLoadCapacity;

    private Double acquisitionCost;

    private Double odometerReading;

    private String region;
}