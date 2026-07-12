package com.odoo.features.trip.controller;

import com.odoo.common.response.ApiResponse;
import com.odoo.features.trip.dto.request.CreateTripRequest;
import com.odoo.features.trip.dto.request.UpdateTripRequest;
import com.odoo.features.trip.dto.response.TripResponse;
import com.odoo.features.trip.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TripResponse> createTrip(
            @Valid @RequestBody CreateTripRequest request
    ) {

        return ApiResponse.success(
                tripService.createTrip(request),
                "Trip created successfully."
        );
    }


    @GetMapping
    public ApiResponse<List<TripResponse>> getAllTrips() {

        return ApiResponse.success(
                tripService.getAllTrips(),
                "Trips fetched successfully."
        );
    }


    @GetMapping("/{id}")
    public ApiResponse<TripResponse> getTripById(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                tripService.getTripById(id),
                "Trip fetched successfully."
        );
    }


    @PutMapping("/{id}")
    public ApiResponse<TripResponse> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTripRequest request
    ) {

        return ApiResponse.success(
                tripService.updateTrip(id, request),
                "Trip updated successfully."
        );
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTrip(
            @PathVariable Long id
    ) {

        tripService.deleteTrip(id);

        return ApiResponse.success(
                null,
                "Trip deleted successfully."
        );
    }


    @PostMapping("/{id}/dispatch")
    public ApiResponse<TripResponse> dispatchTrip(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                tripService.dispatchTrip(id),
                "Trip dispatched successfully."
        );
    }


    @PostMapping("/{id}/complete")
    public ApiResponse<TripResponse> completeTrip(
            @PathVariable Long id,
            @RequestParam Double endOdometer
    ) {

        return ApiResponse.success(
                tripService.completeTrip(id, endOdometer),
                "Trip completed successfully."
        );
    }


    @PostMapping("/{id}/cancel")
    public ApiResponse<TripResponse> cancelTrip(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                tripService.cancelTrip(id),
                "Trip cancelled successfully."
        );
    }

}