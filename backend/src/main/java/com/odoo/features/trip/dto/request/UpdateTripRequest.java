package com.odoo.features.trip.dto.request;

import lombok.Data;

@Data
public class UpdateTripRequest {

    private String source;

    private String destination;

    private Double cargoWeight;

    private Double plannedDistance;

    private Double revenue;
}
