package com.odoo.features.maintenance.dto;

import com.odoo.features.maintenance.entity.MaintenanceStatus;
import lombok.Data;

@Data
public class MaintenanceRequestDTO {
    private Long vehicleId; // Gadi ki ID
    private String issue;
    private String description;
    private Double cost;
    private MaintenanceStatus status;
}