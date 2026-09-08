package com.plantcare.recommendation.dto;

import java.math.BigDecimal;

public class RecommendationRequest {

    private String indoorOutdoor = "INDOOR";
    private String sunlight = "LOW";
    private String locationType = "OFFICE";
    private String maintenanceRequirement = "LOW";
    private BigDecimal maxPrice;

    public RecommendationRequest() {}

    public String getIndoorOutdoor() { return indoorOutdoor; }
    public void setIndoorOutdoor(String indoorOutdoor) { this.indoorOutdoor = indoorOutdoor; }

    public String getSunlight() { return sunlight; }
    public void setSunlight(String sunlight) { this.sunlight = sunlight; }

    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }

    public String getMaintenanceRequirement() { return maintenanceRequirement; }
    public void setMaintenanceRequirement(String maintenanceRequirement) { this.maintenanceRequirement = maintenanceRequirement; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
}
