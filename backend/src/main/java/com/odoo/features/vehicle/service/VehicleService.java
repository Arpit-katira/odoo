package com.odoo.features.vehicle.service;

import com.odoo.common.exception.ConflictException;
import com.odoo.common.exception.ResourceNotFoundException;
import com.odoo.features.vehicle.dto.request.CreateVehicleRequest;
import com.odoo.features.vehicle.dto.request.UpdateVehicleRequest;
import com.odoo.features.vehicle.dto.response.VehicleResponse;
import com.odoo.features.vehicle.entity.Vehicle;
import com.odoo.features.vehicle.mapper.VehicleMapper;
import com.odoo.features.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse createVehicle(CreateVehicleRequest request) {

        if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new ConflictException("Vehicle with this registration number already exists.");
        }

        Vehicle vehicle = VehicleMapper.toEntity(request);

        vehicle = vehicleRepository.save(vehicle);

        return VehicleMapper.toResponse(vehicle);
    }

    public List<VehicleResponse> getAllVehicles() {

        return vehicleRepository.findAll()
                .stream()
                .map(VehicleMapper::toResponse)
                .toList();
    }

    public VehicleResponse getVehicleById(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found.")
                );

        return VehicleMapper.toResponse(vehicle);
    }

    public VehicleResponse updateVehicle(Long id, UpdateVehicleRequest request) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found.")
                );

        vehicle.setModel(request.getModel());
        vehicle.setMaxLoadCapacity(request.getMaxLoadCapacity());
        vehicle.setAcquisitionCost(request.getAcquisitionCost());
        vehicle.setOdometerReading(request.getOdometerReading());
        vehicle.setRegion(request.getRegion());

        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }

        vehicle = vehicleRepository.save(vehicle);

        return VehicleMapper.toResponse(vehicle);
    }

    public void deleteVehicle(Long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found.")
                );

        vehicleRepository.delete(vehicle);
    }
}