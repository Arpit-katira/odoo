package com.odoo.features.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDTO {
    private Long totalVehicles;
    private Long vehiclesInShop;
    private Double totalExpense;
    private Double totalFuelCost;
}