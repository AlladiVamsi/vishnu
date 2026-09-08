package com.plantcare.maintenance.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.maintenance.entity.MaintenancePlan;
import com.plantcare.maintenance.repository.MaintenancePlanRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/maintenance-plans")
@Tag(name = "Maintenance Plans & Subscriptions", description = "Plant Maintenance Subscription Plans REST APIs")
public class MaintenancePlanController {

    private final MaintenancePlanRepository planRepository;

    public MaintenancePlanController(MaintenancePlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @GetMapping
    @Operation(summary = "Get all active plant maintenance subscription plans")
    public ResponseEntity<ApiResponse<List<MaintenancePlan>>> getAllPlans() {
        return ResponseEntity.ok(ApiResponse.success(planRepository.findByActiveTrue()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed maintenance plan by ID")
    public ResponseEntity<ApiResponse<MaintenancePlan>> getPlanById(@PathVariable UUID id) {
        return planRepository.findById(id)
                .map(plan -> ResponseEntity.ok(ApiResponse.success(plan)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
