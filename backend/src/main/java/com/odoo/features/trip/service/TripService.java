package com.odoo.features.trip.service;

import com.odoo.entities.enums.TripStatus;
import com.odoo.features.driver.entity.DriverStatus;
import com.odoo.entities.enums.VehicleStatus;
import com.odoo.common.exception.ConflictException;
import com.odoo.common.exception.ResourceNotFoundException;
import com.odoo.features.driver.entity.Driver;
import com.odoo.features.driver.repository.DriverRepository;
import com.odoo.features.trip.dto.request.CreateTripRequest;
import com.odoo.features.trip.dto.request.UpdateTripRequest;
import com.odoo.features.trip.dto.response.TripResponse;
import com.odoo.features.trip.entity.Trip;
import com.odoo.features.trip.mapper.TripMapper;
import com.odoo.features.trip.repository.TripRepository;
import com.odoo.features.vehicle.entity.Vehicle;
import com.odoo.features.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public TripResponse createTrip(CreateTripRequest request) {

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found.")
                );

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Driver not found.")
                );

        if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new ConflictException("Vehicle is not available.");
        }

        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new ConflictException("Driver is not available.");
        }

        String tripNumber = "TRIP-" + System.currentTimeMillis();

        Trip trip = TripMapper.toEntity(
                request,
                vehicle,
                driver,
                tripNumber
        );

        trip = tripRepository.save(trip);

        return TripMapper.toResponse(trip);
    }


    public List<TripResponse> getAllTrips() {

        return tripRepository.findAll()
                .stream()
                .map(TripMapper::toResponse)
                .toList();
    }


    public TripResponse getTripById(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        return TripMapper.toResponse(trip);
    }


    public TripResponse updateTrip(
            Long id,
            UpdateTripRequest request
    ) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        trip.setSource(request.getSource());
        trip.setDestination(request.getDestination());
        trip.setCargoWeight(request.getCargoWeight());
        trip.setPlannedDistance(request.getPlannedDistance());
        trip.setRevenue(request.getRevenue());

        trip = tripRepository.save(trip);

        return TripMapper.toResponse(trip);
    }


    public void deleteTrip(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        tripRepository.delete(trip);
    }

    public TripResponse dispatchTrip(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        if (trip.getStatus() != TripStatus.DRAFT) {
            throw new ConflictException("Only draft trips can be dispatched.");
        }

        trip.setStatus(TripStatus.DISPATCHED);
        trip.setDispatchedAt(LocalDateTime.now());

        Vehicle vehicle = trip.getVehicle();
        vehicle.setStatus(VehicleStatus.ON_TRIP);

        Driver driver = trip.getDriver();
        driver.setStatus(DriverStatus.ON_TRIP);

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);

        trip = tripRepository.save(trip);

        return TripMapper.toResponse(trip);
    }

    public TripResponse completeTrip(
            Long id,
            Double endOdometer
    ) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        if (trip.getStatus() != TripStatus.DISPATCHED) {
            throw new ConflictException("Trip is not dispatched.");
        }

        trip.setStatus(TripStatus.COMPLETED);
        trip.setCompletedAt(LocalDateTime.now());
        trip.setEndOdometer(endOdometer);

        Vehicle vehicle = trip.getVehicle();
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setOdometerReading(endOdometer);

        Driver driver = trip.getDriver();
        driver.setStatus(DriverStatus.AVAILABLE);

        vehicleRepository.save(vehicle);
        driverRepository.save(driver);

        trip = tripRepository.save(trip);

        return TripMapper.toResponse(trip);
    }

    public TripResponse cancelTrip(Long id) {

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Trip not found.")
                );

        if (trip.getStatus() != TripStatus.DRAFT) {
            throw new ConflictException("Only draft trips can be cancelled.");
        }

        trip.setStatus(TripStatus.CANCELLED);

        trip = tripRepository.save(trip);

        return TripMapper.toResponse(trip);
    }

}