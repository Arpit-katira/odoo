package com.odoo.features.driver.repository;

import com.odoo.features.driver.entity.Driver;
import com.odoo.features.driver.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatus(DriverStatus status);
    boolean existsByLicenseNumber(String licenseNumber);
}