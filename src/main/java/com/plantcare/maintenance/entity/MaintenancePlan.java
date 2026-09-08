package com.plantcare.maintenance.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "maintenance_plans")
public class MaintenancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String frequency = "WEEKLY";

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "watering_included", nullable = false)
    private boolean wateringIncluded = true;

    @Column(name = "cleaning_included", nullable = false)
    private boolean cleaningIncluded = true;

    @Column(name = "health_monitoring_included", nullable = false)
    private boolean healthMonitoringIncluded = true;

    @Column(name = "fertilizer_included", nullable = false)
    private boolean fertilizerIncluded = true;

    @Column(name = "replacement_included", nullable = false)
    private boolean replacementIncluded = false;

    @Column(name = "emergency_support_included", nullable = false)
    private boolean emergencySupportIncluded = false;

    @Column(name = "consultation_included", nullable = false)
    private boolean consultationIncluded = false;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public MaintenancePlan() {}

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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isWateringIncluded() { return wateringIncluded; }
    public void setWateringIncluded(boolean wateringIncluded) { this.wateringIncluded = wateringIncluded; }

    public boolean isCleaningIncluded() { return cleaningIncluded; }
    public void setCleaningIncluded(boolean cleaningIncluded) { this.cleaningIncluded = cleaningIncluded; }

    public boolean isHealthMonitoringIncluded() { return healthMonitoringIncluded; }
    public void setHealthMonitoringIncluded(boolean healthMonitoringIncluded) { this.healthMonitoringIncluded = healthMonitoringIncluded; }

    public boolean isFertilizerIncluded() { return fertilizerIncluded; }
    public void setFertilizerIncluded(boolean fertilizerIncluded) { this.fertilizerIncluded = fertilizerIncluded; }

    public boolean isReplacementIncluded() { return replacementIncluded; }
    public void setReplacementIncluded(boolean replacementIncluded) { this.replacementIncluded = replacementIncluded; }

    public boolean isEmergencySupportIncluded() { return emergencySupportIncluded; }
    public void setEmergencySupportIncluded(boolean emergencySupportIncluded) { this.emergencySupportIncluded = emergencySupportIncluded; }

    public boolean isConsultationIncluded() { return consultationIncluded; }
    public void setConsultationIncluded(boolean consultationIncluded) { this.consultationIncluded = consultationIncluded; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
