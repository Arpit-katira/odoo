package com.odoo.features.trip.dto.response;

import com.odoo.entities.enums.TripStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TripResponse {
    private Long id;
    private String tripNumber;
    private Long vehicleId;
    private String vehicleRegistration;
    private Long driverId;
    private String driverName;
    private String source;
    private String destination;
    private Double cargoWeight;
    private Double plannedDistance;
    private Double startOdometer;
    private Double endOdometer;
    private Double revenue;
    private TripStatus status;
    private LocalDateTime dispatchedAt;
    private LocalDateTime completedAt;
}