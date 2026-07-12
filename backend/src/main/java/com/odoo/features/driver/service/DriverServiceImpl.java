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

    // private final UserRepository userRepository; // Uncomment jab Aryan UserRepository bana de

    @Override
    public Driver addDriver(DriverRequestDTO dto) {
        if (driverRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new RuntimeException("License already registered!");
        }

        Driver driver = new Driver();

        // Agar aapke IDE mein setLicenseNumber() par error aaye,
        // toh Aryan ko bolna ki apne Driver.java (Entity) mein @Data add kare.
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setLicenseCategory(dto.getLicenseCategory());
        driver.setLicenseExpiry(dto.getLicenseExpiry());
        driver.setSafetyScore(100.0);
        driver.setStatus(DriverStatus.AVAILABLE);

        return driverRepository.save(driver);
    }

    @Override
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE);
    }
}