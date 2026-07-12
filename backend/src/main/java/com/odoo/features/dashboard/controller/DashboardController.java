package com.odoo.features.dashboard.controller;

import com.odoo.features.dashboard.dto.DashboardResponseDTO;
import com.odoo.features.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponseDTO getDashboard() {
        return dashboardService.getDashboard();
    }
}