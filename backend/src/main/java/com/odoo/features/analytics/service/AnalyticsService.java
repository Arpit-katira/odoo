package com.odoo.features.analytics.service;

import com.odoo.features.analytics.dto.DashboardStatsDTO;
import com.odoo.features.expense.repository.ExpenseRepository;
import com.odoo.features.fuel.repository.FuelLogRepository;
import com.odoo.features.vehicle.repository.VehicleRepository;
import com.odoo.entities.enums.VehicleStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VehicleRepository vehicleRepository;
    private final ExpenseRepository expenseRepository;
    private final FuelLogRepository fuelLogRepository;

    public DashboardStatsDTO getDashboardStats() {
        long total = vehicleRepository.count();
        long inShop = vehicleRepository.findByStatus(VehicleStatus.IN_SHOP).size();


        Double totalExpense = expenseRepository.findAll().stream()
                .mapToDouble(e -> e.getAmount()).sum();

        Double totalFuel = fuelLogRepository.findAll().stream()
                .mapToDouble(f -> f.getCost()).sum();

        return DashboardStatsDTO.builder()
                .totalVehicles(total)
                .vehiclesInShop(inShop)
                .totalExpense(totalExpense != null ? totalExpense : 0.0)
                .totalFuelCost(totalFuel != null ? totalFuel : 0.0)
                .build();
    }
}