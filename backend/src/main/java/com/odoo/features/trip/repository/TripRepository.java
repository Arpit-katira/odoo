package com.odoo.features.trip.repository;

import com.odoo.entities.enums.TripStatus;
import com.odoo.features.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByStatus(TripStatus status);

    boolean existsByTripNumber(String tripNumber);
}