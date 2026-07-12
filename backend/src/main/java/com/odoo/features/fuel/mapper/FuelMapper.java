package com.odoo.features.fuel.mapper;

import com.odoo.features.fuel.dto.FuelRequestDTO;
import com.odoo.features.fuel.dto.FuelResponseDTO;
import com.odoo.features.fuel.entity.FuelLog;
import com.odoo.features.trip.entity.Trip;

import java.time.LocalDateTime;

public class FuelMapper {

    private FuelMapper() {}

    public static FuelLog toEntity(FuelRequestDTO dto, Trip trip) {

        FuelLog fuel = new FuelLog();

        fuel.setTrip(trip);
        fuel.setLiters(dto.getLiters());
        fuel.setCost(dto.getCost());
        fuel.setOdometerReading(dto.getOdometerReading());
        fuel.setLoggedAt(LocalDateTime.now());

        return fuel;
    }

    public static FuelResponseDTO toDTO(FuelLog fuel) {

        return FuelResponseDTO.builder()
                .id(fuel.getId())
                .tripId(fuel.getTrip().getId())
                .liters(fuel.getLiters())
                .cost(fuel.getCost())
                .odometerReading(fuel.getOdometerReading())
                .loggedAt(fuel.getLoggedAt())
                .build();
    }
}