package com.plantcare.worker.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AssignWorkerRequest {

    @NotNull(message = "Worker ID is required")
    private UUID workerId;

    private String notes;

    public AssignWorkerRequest() {}

    public UUID getWorkerId() { return workerId; }
    public void setWorkerId(UUID workerId) { this.workerId = workerId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
