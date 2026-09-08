package com.plantcare.company.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.company.dto.CompanyLocationResponse;
import com.plantcare.company.dto.CreateCompanyLocationRequest;
import com.plantcare.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "Company Management", description = "Company and Location Management REST APIs")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/locations")
    @Operation(summary = "Add a new location for corporate company")
    public ResponseEntity<ApiResponse<CompanyLocationResponse>> createLocation(@Valid @RequestBody CreateCompanyLocationRequest request) {
        CompanyLocationResponse response = companyService.createLocation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company location created successfully", response));
    }

    @GetMapping("/{companyId}/locations")
    @Operation(summary = "Get all locations belonging to a company")
    public ResponseEntity<ApiResponse<List<CompanyLocationResponse>>> getLocations(@PathVariable UUID companyId) {
        List<CompanyLocationResponse> locations = companyService.getCompanyLocations(companyId);
        return ResponseEntity.ok(ApiResponse.success(locations));
    }

    @GetMapping("/locations/{id}")
    @Operation(summary = "Get detailed company location by ID")
    public ResponseEntity<ApiResponse<CompanyLocationResponse>> getLocationById(@PathVariable UUID id) {
        CompanyLocationResponse location = companyService.getLocationById(id);
        return ResponseEntity.ok(ApiResponse.success(location));
    }
}
