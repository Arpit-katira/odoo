package com.odoo.features.trip.mapper;

import com.odoo.entities.enums.TripStatus;
import com.odoo.features.driver.entity.Driver;
import com.odoo.features.trip.dto.request.CreateTripRequest;
import com.odoo.features.trip.dto.response.TripResponse;
import com.odoo.features.trip.entity.Trip;
import com.odoo.features.vehicle.entity.Vehicle;

public class TripMapper {

    public static Trip toEntity(
            CreateTripRequest request,
            Vehicle vehicle,
            Driver driver,
            String tripNumber
    ) {

        return Trip.builder()
                .tripNumber(tripNumber)
                .vehicle(vehicle)
                .driver(driver)
                .source(request.getSource())
                .destination(request.getDestination())
                .cargoWeight(request.getCargoWeight())
                .plannedDistance(request.getPlannedDistance())
                .revenue(request.getRevenue())
                .startOdometer(vehicle.getOdometerReading())
                .status(TripStatus.DRAFT)
                .build();
    }

    public static TripResponse toResponse(Trip trip) {

        return TripResponse.builder()
                .id(trip.getId())
                .tripNumber(trip.getTripNumber())

                .vehicleId(trip.getVehicle().getId())
                .vehicleRegistration(trip.getVehicle().getRegistrationNumber())

                .driverId(trip.getDriver().getId())
                .driverName(null)

                .source(trip.getSource())
                .destination(trip.getDestination())
                .cargoWeight(trip.getCargoWeight())
                .plannedDistance(trip.getPlannedDistance())

                .startOdometer(trip.getStartOdometer())
                .endOdometer(trip.getEndOdometer())

                .revenue(trip.getRevenue())

                .status(trip.getStatus())

                .dispatchedAt(trip.getDispatchedAt())
                .completedAt(trip.getCompletedAt())

                .build();
    }

}