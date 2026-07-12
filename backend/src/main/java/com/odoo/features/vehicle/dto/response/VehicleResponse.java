package com.odoo.features.vehicle.dto.response;

import com.odoo.entities.enums.VehicleStatus;
import com.odoo.entities.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class VehicleResponse {

    private Long id;

    private String registrationNumber;

    private String model;

    private VehicleType vehicleType;

    private Double maxLoadCapacity;

    private Double acquisitionCost;

    private Double odometerReading;

    private String region;

    private VehicleStatus status;
}