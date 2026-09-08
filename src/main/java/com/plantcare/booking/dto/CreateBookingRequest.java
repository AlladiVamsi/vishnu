package com.plantcare.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateBookingRequest {

    private UUID userId;
    @NotNull(message = "Company location ID is required")
    private UUID locationId;

    private LocalDateTime preferredInstallationDate;
    private boolean siteInspectionRequired = false;
    private UUID maintenancePlanId;

    public CreateBookingRequest() {}

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }

    public LocalDateTime getPreferredInstallationDate() { return preferredInstallationDate; }
    public void setPreferredInstallationDate(LocalDateTime preferredInstallationDate) { this.preferredInstallationDate = preferredInstallationDate; }

    public boolean isSiteInspectionRequired() { return siteInspectionRequired; }
    public void setSiteInspectionRequired(boolean siteInspectionRequired) { this.siteInspectionRequired = siteInspectionRequired; }

    public UUID getMaintenancePlanId() { return maintenancePlanId; }
    public void setMaintenancePlanId(UUID maintenancePlanId) { this.maintenancePlanId = maintenancePlanId; }
}
