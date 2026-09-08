package com.plantcare.worker.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.worker.dto.AssignWorkerRequest;
import com.plantcare.worker.dto.WorkerResponse;
import com.plantcare.worker.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workers")
@Tag(name = "Worker Management", description = "Service Worker & Assignment REST APIs")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping
    @Operation(summary = "Get all worker profiles & availability status")
    public ResponseEntity<ApiResponse<List<WorkerResponse>>> getAllWorkers() {
        return ResponseEntity.ok(ApiResponse.success(workerService.getAllWorkers()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get worker profile details by ID")
    public ResponseEntity<ApiResponse<WorkerResponse>> getWorkerById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(workerService.getWorkerById(id)));
    }

    @PostMapping
    @Operation(summary = "Register a new service worker profile")
    public ResponseEntity<ApiResponse<WorkerResponse>> createWorker(@Valid @RequestBody com.plantcare.worker.dto.CreateWorkerRequest request) {
        WorkerResponse worker = workerService.createWorker(request);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(ApiResponse.success("Worker profile created successfully", worker));
    }

    @PostMapping("/assign/booking/{bookingId}")
    @Operation(summary = "Assign a service worker to a customer booking")
    public ResponseEntity<ApiResponse<Void>> assignWorker(@PathVariable UUID bookingId, @Valid @RequestBody AssignWorkerRequest request) {
        workerService.assignWorkerToBooking(bookingId, request);
        return ResponseEntity.ok(ApiResponse.success("Worker assigned to booking successfully", null));
    }
}
