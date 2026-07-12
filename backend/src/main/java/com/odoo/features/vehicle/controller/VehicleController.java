package com.odoo.features.vehicle.controller;

import com.odoo.common.response.ApiResponse;
import com.odoo.features.vehicle.dto.request.CreateVehicleRequest;
import com.odoo.features.vehicle.dto.request.UpdateVehicleRequest;
import com.odoo.features.vehicle.dto.response.VehicleResponse;
import com.odoo.features.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VehicleResponse> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request
    ) {

        return ApiResponse.success(
                vehicleService.createVehicle(request),
                "Vehicle created successfully."
        );
    }


    @GetMapping
    public ApiResponse<List<VehicleResponse>> getAllVehicles() {

        return ApiResponse.success(
                vehicleService.getAllVehicles(),
                "Vehicles fetched successfully."
        );
    }


    @GetMapping("/{id}")
    public ApiResponse<VehicleResponse> getVehicleById(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                vehicleService.getVehicleById(id),
                "Vehicle fetched successfully."
        );
    }


    @PutMapping("/{id}")
    public ApiResponse<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {

        return ApiResponse.success(
                vehicleService.updateVehicle(id, request),
                "Vehicle updated successfully."
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> deleteVehicle(
            @PathVariable Long id
    ) {

        vehicleService.deleteVehicle(id);

        return ApiResponse.success(
                null,
                "Vehicle deleted successfully."
        );
    }
}