package com.odoo.features.dashboard.service;

import com.odoo.entities.enums.TripStatus;
import com.odoo.entities.enums.VehicleStatus;
import com.odoo.features.dashboard.dto.ActiveTripDTO;
import com.odoo.features.dashboard.dto.DashboardResponseDTO;
import com.odoo.features.dashboard.dto.DashboardStatsDTO;
import com.odoo.features.dashboard.dto.VehicleSummaryDTO;
import com.odoo.features.driver.entity.Driver;
import com.odoo.features.driver.entity.DriverStatus;
import com.odoo.features.driver.repository.DriverRepository;
import com.odoo.features.trip.entity.Trip;
import com.odoo.features.trip.repository.TripRepository;
import com.odoo.features.vehicle.entity.Vehicle;
import com.odoo.features.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final TripRepository tripRepository;

    @Override

    public DashboardResponseDTO getDashboard() {


        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Driver> drivers = driverRepository.findAll();
        List<Trip> trips = tripRepository.findAll();

        long activeVehicles =
                vehicles.stream().filter(v -> v.getStatus() == VehicleStatus.ON_TRIP).count();

        long availableVehicles =
                vehicles.stream().filter(v -> v.getStatus() == VehicleStatus.AVAILABLE).count();

        long inMaintenance =
                vehicles.stream().filter(v -> v.getStatus() == VehicleStatus.IN_SHOP).count();

        long activeTrips =
                trips.stream().filter(t -> t.getStatus() == TripStatus.DISPATCHED).count();

        long pendingTrips =
                trips.stream().filter(t -> t.getStatus() == TripStatus.DRAFT).count();

        long driversOnDuty =
                drivers.stream().filter(d -> d.getStatus() == DriverStatus.ON_TRIP).count();

        long operational =
                vehicles.stream().filter(v -> v.getStatus() != VehicleStatus.RETIRED).count();

        int utilization = operational == 0
                ? 0
                : (int)((activeVehicles * 100) / operational);

        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .activeVehicles(activeVehicles)
                .availableVehicles(availableVehicles)
                .inMaintenance(inMaintenance)
                .activeTrips(activeTrips)
                .pendingTrips(pendingTrips)
                .driversOnDuty(driversOnDuty)
                .fleetUtilization(utilization)
                .build();

        List<VehicleSummaryDTO> vehicleList =
                vehicles.stream()
                        .map(v -> VehicleSummaryDTO.builder()
                                .id(v.getId())
                                .registrationNumber(v.getRegistrationNumber())
                                .model(v.getModel())
                                .vehicleType(v.getVehicleType().name())
                                .region(v.getRegion())
                                .status(v.getStatus().name())
                                .build())
                        .toList();

        List<ActiveTripDTO> activeTripList =
                trips.stream()
                        .filter(t -> t.getStatus() == TripStatus.DISPATCHED)
                        .map(t -> ActiveTripDTO.builder()
                                .id(t.getId())
                                .source(t.getSource())
                                .destination(t.getDestination())
                                .vehicle(t.getVehicle().getModel())
                                .driver(t.getDriver().getUser().getFullName())
                                .cargoWeight(t.getCargoWeight())
                                .plannedDistance(t.getPlannedDistance())
                                .status(t.getStatus().name())
                                .build())
                        .toList();

        return DashboardResponseDTO.builder()
                .stats(stats)
                .vehicles(vehicleList)
                .activeTrips(activeTripList)
                .build();
    }
}