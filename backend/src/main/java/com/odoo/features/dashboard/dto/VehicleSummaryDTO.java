package com.odoo.features.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleSummaryDTO {

    private Long id;

    private String registrationNumber;

    private String model;

    private String vehicleType;

    private String region;

    private String status;
}