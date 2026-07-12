package com.odoo.features.maintenance.service;

import com.odoo.entities.enums.VehicleStatus;
import com.odoo.features.maintenance.dto.MaintenanceRequestDTO;
import com.odoo.features.maintenance.entity.MaintenanceRecord;
import com.odoo.features.maintenance.entity.MaintenanceStatus;
import com.odoo.features.maintenance.repository.MaintenanceRepository;
import com.odoo.features.vehicle.entity.Vehicle;
import com.odoo.features.vehicle.repository.VehicleRepository;
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

        if (dto.getStatus() == MaintenanceStatus.OPEN) {
            record.setStartedAt(LocalDateTime.now());
        } else if (dto.getStatus() == MaintenanceStatus.CLOSED) {
            record.setCompletedAt(LocalDateTime.now());
        }

        if (dto.getVehicleId() != null) {

            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() ->
                            new RuntimeException("Vehicle not found with ID: " + dto.getVehicleId()));

            record.setVehicle(vehicle);

            if (dto.getStatus() == MaintenanceStatus.OPEN) {
                vehicle.setStatus(VehicleStatus.IN_SHOP);
            } else if (dto.getStatus() == MaintenanceStatus.CLOSED) {
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

    @Override
    public MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRequestDTO dto) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found with ID: " + id));

        MaintenanceStatus oldStatus = record.getStatus();
        MaintenanceStatus newStatus = dto.getStatus();

        record.setIssue(dto.getIssue());
        record.setDescription(dto.getDescription());
        record.setCost(dto.getCost());
        record.setStatus(newStatus);

        if (newStatus == MaintenanceStatus.OPEN && oldStatus != MaintenanceStatus.OPEN) {
            record.setStartedAt(LocalDateTime.now());
            record.setCompletedAt(null);
        } else if (newStatus == MaintenanceStatus.CLOSED && oldStatus != MaintenanceStatus.CLOSED) {
            record.setCompletedAt(LocalDateTime.now());
        }

        Vehicle vehicle = record.getVehicle();
        if (vehicle != null) {
            if (newStatus == MaintenanceStatus.OPEN) {
                vehicle.setStatus(VehicleStatus.IN_SHOP);
            } else if (newStatus == MaintenanceStatus.CLOSED) {
                vehicle.setStatus(VehicleStatus.AVAILABLE);
            }
            vehicleRepository.save(vehicle);
        }

        return maintenanceRepository.save(record);
    }

    @Override
    public void deleteMaintenanceRecord(Long id) {
        MaintenanceRecord record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found with ID: " + id));
        maintenanceRepository.delete(record);
    }
}
