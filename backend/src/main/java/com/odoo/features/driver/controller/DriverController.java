package com.odoo.features.driver.controller;

import com.odoo.common.response.ApiResponse; // Naya import
import com.odoo.features.driver.entity.Driver;
import com.odoo.features.driver.dto.DriverRequestDTO;
import com.odoo.features.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<ApiResponse<Driver>> addDriver(@RequestBody DriverRequestDTO request) {
        Driver newDriver = driverService.addDriver(request);

        return new ResponseEntity<>(
                ApiResponse.success(newDriver, "Driver registered successfully!"),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Driver>>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        String message = drivers.isEmpty() ? "No drivers found." : "All drivers fetched.";
        return ResponseEntity.ok(ApiResponse.success(drivers, message));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Driver>> getDriverById(@PathVariable Long id) {
        Driver driver = driverService.getDriverById(id);
        return ResponseEntity.ok(ApiResponse.success(driver, "Driver fetched successfully."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Driver>> updateDriver(
            @PathVariable Long id,
            @RequestBody DriverRequestDTO request) {
        Driver updatedDriver = driverService.updateDriver(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedDriver, "Driver updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Driver deleted successfully."));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Driver>>> getAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();

        String message = drivers.isEmpty() ? "No available drivers found." : "Available drivers fetched.";

        return ResponseEntity.ok(ApiResponse.success(drivers, message));
    }
}
