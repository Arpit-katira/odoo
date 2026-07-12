package com.odoo.features.maintenance.service;

import com.odoo.features.maintenance.dto.MaintenanceRequestDTO;
import com.odoo.features.maintenance.entity.MaintenanceRecord;
import com.odoo.features.maintenance.entity.MaintenanceStatus;
import com.odoo.features.maintenance.repository.MaintenanceRepository;


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


        if (dto.getVehicleId() != null) {

            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + dto.getVehicleId()));


            record.setVehicle(vehicle);


            if (dto.getStatus() == MaintenanceStatus.IN_PROGRESS) {
                vehicle.setStatus(VehicleStatus.IN_SHOP);
            } else if (dto.getStatus() == MaintenanceStatus.COMPLETED) {
                vehicle.setStatus(VehicleStatus.AVAILABLE);
            }


            vehicleRepository.save(vehicle);
        }

        return maintenanceRepository.save(record);
    }

    @Override
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRepository.findAll();
    }
}