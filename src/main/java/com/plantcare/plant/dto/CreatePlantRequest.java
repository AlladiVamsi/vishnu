package com.plantcare.plant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public class CreatePlantRequest {

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotBlank(message = "Plant name is required")
    private String name;

    private String scientificName;
    private String description;

    @NotNull(message = "Purchase price is required")
    @PositiveOrZero(message = "Purchase price must be positive or zero")
    private BigDecimal purchasePrice;

    @NotNull(message = "Rental price is required")
    @PositiveOrZero(message = "Rental price must be positive or zero")
    private BigDecimal rentalPrice;

    @PositiveOrZero(message = "Installation charge must be positive or zero")
    private BigDecimal installationCharge = BigDecimal.ZERO;

    private String size;
    private String height;
    private String indoorOutdoor = "INDOOR";
    private String sunlightRequirement;
    private String waterRequirement;
    private String temperatureRequirement;
    private String humidityRequirement;
    private String maintenanceLevel;
    private String maintenanceFrequency;
    private String suitableLocations;
    private String careInstructions;
    private boolean saleAvailable = true;
    private boolean rentalAvailable = true;
    
    @Min(value = 0, message = "Initial inventory quantity cannot be negative")
    private int initialInventory = 10;

    public CreatePlantRequest() {}

    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(BigDecimal rentalPrice) { this.rentalPrice = rentalPrice; }

    public BigDecimal getInstallationCharge() { return installationCharge; }
    public void setInstallationCharge(BigDecimal installationCharge) { this.installationCharge = installationCharge; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getIndoorOutdoor() { return indoorOutdoor; }
    public void setIndoorOutdoor(String indoorOutdoor) { this.indoorOutdoor = indoorOutdoor; }

    public String getSunlightRequirement() { return sunlightRequirement; }
    public void setSunlightRequirement(String sunlightRequirement) { this.sunlightRequirement = sunlightRequirement; }

    public String getWaterRequirement() { return waterRequirement; }
    public void setWaterRequirement(String waterRequirement) { this.waterRequirement = waterRequirement; }

    public String getTemperatureRequirement() { return temperatureRequirement; }
    public void setTemperatureRequirement(String temperatureRequirement) { this.temperatureRequirement = temperatureRequirement; }

    public String getHumidityRequirement() { return humidityRequirement; }
    public void setHumidityRequirement(String humidityRequirement) { this.humidityRequirement = humidityRequirement; }

    public String getMaintenanceLevel() { return maintenanceLevel; }
    public void setMaintenanceLevel(String maintenanceLevel) { this.maintenanceLevel = maintenanceLevel; }

    public String getMaintenanceFrequency() { return maintenanceFrequency; }
    public void setMaintenanceFrequency(String maintenanceFrequency) { this.maintenanceFrequency = maintenanceFrequency; }

    public String getSuitableLocations() { return suitableLocations; }
    public void setSuitableLocations(String suitableLocations) { this.suitableLocations = suitableLocations; }

    public String getCareInstructions() { return careInstructions; }
    public void setCareInstructions(String careInstructions) { this.careInstructions = careInstructions; }

    public boolean isSaleAvailable() { return saleAvailable; }
    public void setSaleAvailable(boolean saleAvailable) { this.saleAvailable = saleAvailable; }

    public boolean isRentalAvailable() { return rentalAvailable; }
    public void setRentalAvailable(boolean rentalAvailable) { this.rentalAvailable = rentalAvailable; }

    public int getInitialInventory() { return initialInventory; }
    public void setInitialInventory(int initialInventory) { this.initialInventory = initialInventory; }
}
