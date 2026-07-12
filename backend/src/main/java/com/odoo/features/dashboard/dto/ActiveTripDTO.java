package com.odoo.features.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveTripDTO {

    private Long id;

    private String source;

    private String destination;

    private String vehicle;

    private String driver;

    private Double cargoWeight;

    private Double plannedDistance;

    private String status;
}