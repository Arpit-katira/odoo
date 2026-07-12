package com.odoo.features.maintenance.controller;

import com.odoo.common.response.ApiResponse;
import com.odoo.features.maintenance.dto.MaintenanceRequestDTO;
import com.odoo.features.maintenance.entity.MaintenanceRecord;
import com.odoo.features.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<ApiResponse<MaintenanceRecord>> logMaintenance(@RequestBody MaintenanceRequestDTO request) {
        MaintenanceRecord record = maintenanceService.logMaintenance(request);
        return new ResponseEntity<>(
                ApiResponse.success(record, "Maintenance record logged successfully"),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MaintenanceRecord>>> getAllRecords() {
        return ResponseEntity.ok(ApiResponse.success(
                maintenanceService.getAllMaintenanceRecords(),
                "All maintenance records fetched"
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MaintenanceRecord>> updateMaintenanceRecord(
            @PathVariable Long id,
            @RequestBody MaintenanceRequestDTO request) {
        MaintenanceRecord record = maintenanceService.updateMaintenanceRecord(id, request);
        return ResponseEntity.ok(ApiResponse.success(record, "Maintenance record updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMaintenanceRecord(@PathVariable Long id) {
        maintenanceService.deleteMaintenanceRecord(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Maintenance record deleted successfully"));
    }
}
