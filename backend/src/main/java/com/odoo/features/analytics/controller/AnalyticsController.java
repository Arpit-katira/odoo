package com.odoo.features.analytics.controller;

import com.odoo.common.response.ApiResponse;
import com.odoo.features.analytics.dto.DashboardStatsDTO;
import com.odoo.features.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardStatsDTO> getDashboard() {
        return ApiResponse.success(analyticsService.getDashboardStats(), "Dashboard stats fetched.");
    }
}