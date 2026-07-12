package com.odoo.features.driver.dto;

import com.odoo.features.driver.entity.LicenseCategory;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DriverRequestDTO {
    private Long userId;
    private String licenseNumber;
    private LicenseCategory licenseCategory;
    private LocalDate licenseExpiry;
}