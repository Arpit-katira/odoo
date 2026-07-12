package com.odoo.features.vehicle.mapper;

import com.odoo.entities.enums.VehicleStatus;
import com.odoo.features.vehicle.dto.request.CreateVehicleRequest;
import com.odoo.features.vehicle.dto.response.VehicleResponse;
import com.odoo.features.vehicle.entity.Vehicle;

public class VehicleMapper {

    public static Vehicle toEntity(CreateVehicleRequest request) {

        return Vehicle.builder()
                .registrationNumber(request.getRegistrationNumber())
                .model(request.getModel())
                .vehicleType(request.getVehicleType())
                .maxLoadCapacity(request.getMaxLoadCapacity())
                .acquisitionCost(request.getAcquisitionCost())
                .odometerReading(request.getOdometerReading())
                .region(request.getRegion())
                .status(VehicleStatus.AVAILABLE)
                .build();
    }

    public static VehicleResponse toResponse(Vehicle vehicle) {

        return VehicleResponse.builder()
                .id(vehicle.getId())
                .registrationNumber(vehicle.getRegistrationNumber())
                .model(vehicle.getModel())
                .vehicleType(vehicle.getVehicleType())
                .maxLoadCapacity(vehicle.getMaxLoadCapacity())
                .acquisitionCost(vehicle.getAcquisitionCost())
                .odometerReading(vehicle.getOdometerReading())
                .region(vehicle.getRegion())
                .status(vehicle.getStatus())
                .build();
    }
}