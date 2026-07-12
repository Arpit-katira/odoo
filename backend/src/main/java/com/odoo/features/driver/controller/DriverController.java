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
        // ApiResponse.success() use kar rahe hain message ke sath
        return new ResponseEntity<>(
                ApiResponse.success(newDriver, "Driver registered successfully!"),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Driver>>> getAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();
        // Agar list khali hai toh message alag bhej sakte hain
        String message = drivers.isEmpty() ? "No available drivers found." : "Available drivers fetched.";

        return ResponseEntity.ok(ApiResponse.success(drivers, message));
    }
}