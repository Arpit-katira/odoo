package com.odoo.features.driver.controller;

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
    public ResponseEntity<Driver> addDriver(@RequestBody DriverRequestDTO request) {
        Driver newDriver = driverService.addDriver(request);
        return new ResponseEntity<>(newDriver, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }
}