package com.odoo.features.vehicle.dto.request;

import com.odoo.entities.enums.VehicleStatus;
import lombok.Data;



@Data
public class UpdateVehicleRequest {

    private String model;

    private Double maxLoadCapacity;

    private Double acquisitionCost;

    private Double odometerReading;

    private String region;

    private VehicleStatus status;
}