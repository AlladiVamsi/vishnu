package com.plantcare.plant.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.common.response.PageResponse;
import com.plantcare.plant.dto.CreatePlantRequest;
import com.plantcare.plant.dto.PlantImageResponse;
import com.plantcare.plant.dto.PlantResponse;
import com.plantcare.plant.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plants")
@Tag(name = "Plant Catalog", description = "Plant Catalog Search, View, Upload & Management REST APIs")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    @Operation(summary = "Search and filter plant catalog with pagination")
    public ResponseEntity<ApiResponse<PageResponse<PlantResponse>>> getPlants(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String indoorOutdoor,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageResponse<PlantResponse> result = plantService.searchPlants(categoryId, indoorOutdoor, minPrice, maxPrice, search, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get plant details by ID")
    public ResponseEntity<ApiResponse<PlantResponse>> getPlantById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(plantService.getPlantById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new plant catalog entry")
    public ResponseEntity<ApiResponse<PlantResponse>> createPlant(@Valid @RequestBody CreatePlantRequest request) {
        PlantResponse response = plantService.createPlant(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Plant created successfully", response));
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image for plant")
    public ResponseEntity<ApiResponse<PlantImageResponse>> uploadImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "false") boolean primaryImage) {
        PlantImageResponse response = plantService.uploadPlantImage(id, file, primaryImage);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Image uploaded successfully", response));
    }
}
