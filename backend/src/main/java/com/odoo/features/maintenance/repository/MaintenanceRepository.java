package com.odoo.features.maintenance.repository;

import com.odoo.features.maintenance.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
}