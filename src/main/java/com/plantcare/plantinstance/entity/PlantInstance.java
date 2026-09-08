package com.plantcare.plantinstance.entity;

import com.plantcare.booking.entity.Booking;
import com.plantcare.company.entity.Company;
import com.plantcare.company.entity.CompanyLocation;
import com.plantcare.plant.entity.Plant;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "plant_instances", indexes = {
        @Index(name = "idx_plant_instances_code", columnList = "unique_plant_code"),
        @Index(name = "idx_plant_instances_qr", columnList = "qr_code")
})
public class PlantInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private CompanyLocation location;

    @Column(name = "unique_plant_code", nullable = false, unique = true, length = 100)
    private String uniquePlantCode;

    @Column(name = "qr_code", nullable = false, unique = true, length = 255)
    private String qrCode;

    @Column(name = "installation_date", nullable = false)
    private LocalDate installationDate;

    @Column(name = "current_health_status", nullable = false, length = 30)
    private String currentHealthStatus = "HEALTHY"; // HEALTHY, NEEDS_ATTENTION, UNHEALTHY, CRITICAL, REPLACED

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PlantInstance() {}

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

    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public CompanyLocation getLocation() { return location; }
    public void setLocation(CompanyLocation location) { this.location = location; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
