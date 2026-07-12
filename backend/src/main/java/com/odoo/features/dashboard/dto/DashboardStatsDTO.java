package com.odoo.features.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDTO {

    private Long activeVehicles;

    private Long availableVehicles;

    private Long inMaintenance;

    private Long activeTrips;

    private Long pendingTrips;

    private Long driversOnDuty;

    private Integer fleetUtilization;
}