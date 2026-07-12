package com.odoo.features.fuel.service;

import com.odoo.features.fuel.dto.*;
import com.odoo.features.fuel.entity.FuelLog;
import com.odoo.features.fuel.mapper.FuelMapper;
import com.odoo.features.fuel.repository.FuelLogRepository;
import com.odoo.features.trip.entity.Trip;
import com.odoo.features.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuelServiceImpl implements FuelService {

    private final FuelLogRepository fuelRepository;
    private final TripRepository tripRepository;

    @Override
    public FuelResponseDTO create(FuelRequestDTO dto) {

        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        FuelLog fuel = FuelMapper.toEntity(dto, trip);

        return FuelMapper.toDTO(fuelRepository.save(fuel));
    }

    @Override
    public List<FuelResponseDTO> getAll() {

        return fuelRepository.findAll()
                .stream()
                .map(FuelMapper::toDTO)
                .toList();
    }

    @Override
    public FuelResponseDTO getById(Long id) {

        FuelLog fuel = fuelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel log not found"));

        return FuelMapper.toDTO(fuel);
    }

    @Override
    public FuelResponseDTO update(Long id, FuelRequestDTO dto) {

        FuelLog fuel = fuelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel log not found"));

        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        fuel.setTrip(trip);
        fuel.setLiters(dto.getLiters());
        fuel.setCost(dto.getCost());
        fuel.setOdometerReading(dto.getOdometerReading());

        return FuelMapper.toDTO(fuelRepository.save(fuel));
    }

    @Override
    public void delete(Long id) {

        fuelRepository.deleteById(id);
    }
}