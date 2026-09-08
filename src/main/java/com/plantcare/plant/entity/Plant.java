package com.plantcare.plant.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "plants", indexes = {
        @Index(name = "idx_plants_name", columnList = "name"),
        @Index(name = "idx_plants_cat", columnList = "category_id")
})
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PlantCategory category;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "scientific_name", length = 150)
    private String scientificName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "rental_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal rentalPrice;

    @Column(name = "installation_charge", nullable = false, precision = 10, scale = 2)
    private BigDecimal installationCharge = BigDecimal.ZERO;

    @Column(length = 50)
    private String size;

    @Column(length = 50)
    private String height;

    @Column(name = "indoor_outdoor", nullable = false, length = 30)
    private String indoorOutdoor = "INDOOR";

    @Column(name = "sunlight_requirement", length = 50)
    private String sunlightRequirement;

    @Column(name = "water_requirement", length = 50)
    private String waterRequirement;

    @Column(name = "temperature_requirement", length = 50)
    private String temperatureRequirement;

    @Column(name = "humidity_requirement", length = 50)
    private String humidityRequirement;

    @Column(name = "maintenance_level", length = 50)
    private String maintenanceLevel;

    @Column(name = "maintenance_frequency", length = 50)
    private String maintenanceFrequency;

    @Column(name = "suitable_locations", columnDefinition = "TEXT")
    private String suitableLocations;

    @Column(name = "care_instructions", columnDefinition = "TEXT")
    private String careInstructions;

    @Column(name = "sale_available", nullable = false)
    private boolean saleAvailable = true;

    @Column(name = "rental_available", nullable = false)
    private boolean rentalAvailable = true;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlantImage> images = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Plant() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public PlantCategory getCategory() { return category; }
    public void setCategory(PlantCategory category) { this.category = category; }

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<PlantImage> getImages() { return images; }
    public void setImages(List<PlantImage> images) { this.images = images; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
