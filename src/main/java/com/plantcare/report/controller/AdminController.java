package com.plantcare.report.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.greenoffice.service.GreenOfficeScoreService;
import com.plantcare.report.service.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Dashboard & Reports", description = "Administrative Dashboard Metrics & Corporate Green Scoring REST APIs")
public class AdminController {

    private final AdminReportService reportService;
    private final GreenOfficeScoreService greenOfficeScoreService;

    public AdminController(AdminReportService reportService, GreenOfficeScoreService greenOfficeScoreService) {
        this.reportService = reportService;
        this.greenOfficeScoreService = greenOfficeScoreService;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get aggregated administrative dashboard metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getAdminDashboardMetrics()));
    }

    @GetMapping("/green-score/company/{companyId}")
    @Operation(summary = "Calculate corporate Green Office Score (0-100)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGreenScore(@PathVariable UUID companyId) {
        return ResponseEntity.ok(ApiResponse.success(greenOfficeScoreService.calculateGreenOfficeScore(companyId)));
    }
}
