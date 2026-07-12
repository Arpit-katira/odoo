package com.odoo.features.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardResponseDTO {

    private DashboardStatsDTO stats;

    private List<VehicleSummaryDTO> vehicles;

    private List<ActiveTripDTO> activeTrips;
}