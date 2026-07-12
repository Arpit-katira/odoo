package com.odoo.features.trip.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompleteTripRequest {

    @NotNull
    private Double endOdometer;
}