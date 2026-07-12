package com.odoo.features.maintenance.service;

import com.odoo.features.maintenance.dto.MaintenanceRequestDTO;
import com.odoo.features.maintenance.entity.MaintenanceRecord;
import java.util.List;

public interface MaintenanceService {
    MaintenanceRecord logMaintenance(MaintenanceRequestDTO dto);
    List<MaintenanceRecord> getAllMaintenanceRecords();
    MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRequestDTO dto);
    void deleteMaintenanceRecord(Long id);
}
