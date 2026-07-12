package com.odoo.features.vehicle.repository;

import com.odoo.features.vehicle.entity.Vehicle;
import com.odoo.entities.enums.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByStatus(VehicleStatus status);

    boolean existsByRegistrationNumber(String registrationNumber);
}