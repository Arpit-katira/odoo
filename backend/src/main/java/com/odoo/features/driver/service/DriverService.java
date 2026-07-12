package com.odoo.features.driver.service;

import com.odoo.features.driver.entity.Driver;
import com.odoo.features.driver.dto.DriverRequestDTO;
import java.util.List;

public interface DriverService {
    Driver addDriver(DriverRequestDTO requestDTO);
    List<Driver> getAvailableDrivers();
}