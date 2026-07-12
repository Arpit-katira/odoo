package com.odoo.features.analytics.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DashboardStatsDTO {
    private Long totalVehicles;
    private Long vehiclesInShop; // Maintenance mein kitni hain
    private Double totalExpense;
    private Double totalFuelCost;
    private Map<String, Double> expenseByCategory; // Toll, Food, etc.
}