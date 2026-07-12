package com.odoo.features.driver.dto;

import com.odoo.features.driver.entity.LicenseCategory;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DriverRequestDTO {
    private Long userId; // Jab User entity set ho jayegi, tab isko use karenge
    private String licenseNumber;
    private LicenseCategory licenseCategory;
    private LocalDate licenseExpiry;
}