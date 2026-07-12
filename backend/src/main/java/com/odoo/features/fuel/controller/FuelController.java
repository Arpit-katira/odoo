package com.odoo.features.fuel.controller;

import com.odoo.common.response.ApiResponse;
import com.odoo.features.fuel.dto.*;
import com.odoo.features.fuel.service.FuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
public class FuelController {

    private final FuelService fuelService;

    @PostMapping
    public ApiResponse<FuelResponseDTO> create(@RequestBody FuelRequestDTO dto) {
        return ApiResponse.success(fuelService.create(dto), "Fuel logged successfully.");
    }

    @GetMapping
    public ApiResponse<List<FuelResponseDTO>> getAll() {
        return ApiResponse.success(fuelService.getAll(), "Fuel logs fetched.");
    }

    @GetMapping("/{id}")
    public ApiResponse<FuelResponseDTO> getById(@PathVariable Long id) {
        return ApiResponse.success(fuelService.getById(id), "Fuel log fetched.");
    }

    @PutMapping("/{id}")
    public ApiResponse<FuelResponseDTO> update(@PathVariable Long id, @RequestBody FuelRequestDTO dto) {
        return ApiResponse.success(fuelService.update(id, dto), "Fuel log updated.");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        fuelService.delete(id);
        return ApiResponse.success(null, "Fuel log deleted.");
    }
}