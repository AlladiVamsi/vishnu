package com.plantcare.plantinstance.dto;

import java.time.LocalDate;
import java.util.UUID;

public class PlantInstanceResponse {

    private UUID id;
    private UUID plantId;
    private String plantName;
    private UUID bookingId;
    private UUID companyId;
    private String companyName;
    private UUID locationId;
    private String locationName;
    private String uniquePlantCode;
    private String qrCode;
    private LocalDate installationDate;
    private String currentHealthStatus;
    private boolean active;

    public PlantInstanceResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPlantId() { return plantId; }
    public void setPlantId(UUID plantId) { this.plantId = plantId; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public UUID getBookingId() { return bookingId; }
    public void setBookingId(UUID bookingId) { this.bookingId = bookingId; }

    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getUniquePlantCode() { return uniquePlantCode; }
    public void setUniquePlantCode(String uniquePlantCode) { this.uniquePlantCode = uniquePlantCode; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }

    public String getCurrentHealthStatus() { return currentHealthStatus; }
    public void setCurrentHealthStatus(String currentHealthStatus) { this.currentHealthStatus = currentHealthStatus; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
