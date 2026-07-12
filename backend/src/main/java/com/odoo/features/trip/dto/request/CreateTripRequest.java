package com.odoo.features.trip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTripRequest {

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long driverId;

    @NotBlank
    private String source;

    @NotBlank
    private String destination;

    @NotNull
    private Double cargoWeight;

    @NotNull
    private Double plannedDistance;

    private Double revenue;
}