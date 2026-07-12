package com.odoo.features.maintenance.service;

import com.odoo.features.maintenance.dto.MaintenanceRequestDTO;
import com.odoo.features.maintenance.entity.MaintenanceRecord;
import com.odoo.features.maintenance.entity.MaintenanceStatus;
import com.odoo.features.maintenance.repository.MaintenanceRepository;

// Naye aur activated imports
import com.odoo.features.vehicle.entity.Vehicle;
import com.odoo.features.vehicle.repository.VehicleRepository;
import com.odoo.entities.enums.VehicleStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    // Ab yeh active ho gaya hai kyunki Aryan ne bana diya hai!
    private final VehicleRepository vehicleRepository;

    @Override
    public MaintenanceRecord logMaintenance(MaintenanceRequestDTO dto) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setIssue(dto.getIssue());
        record.setDescription(dto.getDescription());
        record.setCost(dto.getCost());
        record.setStatus(dto.getStatus());

        if (dto.getStatus() == MaintenanceStatus.IN_PROGRESS) {
            record.setStartedAt(LocalDateTime.now());
        } else if (dto.getStatus() == MaintenanceStatus.COMPLETED) {
            record.setCompletedAt(LocalDateTime.now());
        }

        // 🔥 THE TRIGGER LOGIC 🔥
        if (dto.getVehicleId() != null) {
            // 1. Gadi dhoondho
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + dto.getVehicleId()));

            // 2. Record mein gadi link karo
            record.setVehicle(vehicle);

            // 3. Gadi ka status automatically change karo!
            if (dto.getStatus() == MaintenanceStatus.IN_PROGRESS) {
                vehicle.setStatus(VehicleStatus.IN_SHOP); // Gadi workshop mein gayi
            } else if (dto.getStatus() == MaintenanceStatus.COMPLETED) {
                vehicle.setStatus(VehicleStatus.AVAILABLE); // Gadi theek ho kar aagayi
            }

            // 4. Gadi ka naya status save karo
            vehicleRepository.save(vehicle);
        }

        return maintenanceRepository.save(record);
    }

    @Override
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRepository.findAll();
    }
}