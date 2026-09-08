package com.plantcare.plantinstance.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.plantinstance.dto.PlantInstanceResponse;
import com.plantcare.plantinstance.service.PlantInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plant-instances")
@Tag(name = "Plant Physical Tracking", description = "Physical Plant Instances & QR Code Tracking REST APIs")
public class PlantInstanceController {

    private final PlantInstanceService instanceService;

    public PlantInstanceController(PlantInstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @GetMapping("/qr/{code}")
    @Operation(summary = "QR Code Scan Lookup - Get plant info, health & maintenance history")
    public ResponseEntity<ApiResponse<PlantInstanceResponse>> getByQrCode(@PathVariable String code) {
        PlantInstanceResponse instance = instanceService.getByQrCode(code);
        return ResponseEntity.ok(ApiResponse.success(instance));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get physical plant instance record by ID")
    public ResponseEntity<ApiResponse<PlantInstanceResponse>> getById(@PathVariable UUID id) {
        PlantInstanceResponse instance = instanceService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(instance));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all physical plant instances installed at a company")
    public ResponseEntity<ApiResponse<List<PlantInstanceResponse>>> getCompanyInstances(@PathVariable UUID companyId) {
        List<PlantInstanceResponse> instances = instanceService.getCompanyInstances(companyId);
        return ResponseEntity.ok(ApiResponse.success(instances));
    }
}
