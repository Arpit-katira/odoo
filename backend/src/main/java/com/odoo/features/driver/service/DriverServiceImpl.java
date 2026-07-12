package com.odoo.features.driver.service;

import com.odoo.features.driver.entity.Driver;
import com.odoo.features.driver.entity.DriverStatus;
import com.odoo.features.driver.dto.DriverRequestDTO;
import com.odoo.features.driver.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;



    @Override
    public Driver addDriver(DriverRequestDTO dto) {
        if (driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new RuntimeException("License already registered!");
        }

        Driver driver = new Driver();


        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setLicenseCategory(dto.getLicenseCategory());
        driver.setLicenseExpiry(dto.getLicenseExpiry());
        driver.setSafetyScore(100.0);
        driver.setStatus(DriverStatus.AVAILABLE);

        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Override
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + id));
    }

    @Override
    public Driver updateDriver(Long id, DriverRequestDTO dto) {
        Driver driver = getDriverById(id);

        if (!driver.getLicenseNumber().equals(dto.getLicenseNumber())
                && driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new RuntimeException("License already registered!");
        }

        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setLicenseCategory(dto.getLicenseCategory());
        driver.setLicenseExpiry(dto.getLicenseExpiry());

        return driverRepository.save(driver);
    }

    @Override
    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        driverRepository.delete(driver);
    }

    @Override
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE);
    }
}
