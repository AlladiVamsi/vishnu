package com.plantcare.inspection.entity;

import com.plantcare.auth.entity.User;
import com.plantcare.booking.entity.Booking;
import com.plantcare.company.entity.Company;
import com.plantcare.company.entity.CompanyLocation;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "site_inspections")
public class SiteInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private CompanyLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_expert_id")
    private User assignedExpert;

    @Column(name = "requested_date", nullable = false)
    private LocalDateTime requestedDate;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "available_space", precision = 10, scale = 2)
    private BigDecimal availableSpace;

    @Column(name = "sunlight_level", length = 50)
    private String sunlightLevel;

    @Column(length = 50)
    private String temperature;

    @Column(length = 50)
    private String humidity;

    @Column(name = "indoor_outdoor", length = 30)
    private String indoorOutdoor;

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, length = 30)
    private String status = "REQUESTED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public SiteInspection() {}

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

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public CompanyLocation getLocation() { return location; }
    public void setLocation(CompanyLocation location) { this.location = location; }

    public User getAssignedExpert() { return assignedExpert; }
    public void setAssignedExpert(User assignedExpert) { this.assignedExpert = assignedExpert; }

    public LocalDateTime getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDateTime requestedDate) { this.requestedDate = requestedDate; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    public BigDecimal getAvailableSpace() { return availableSpace; }
    public void setAvailableSpace(BigDecimal availableSpace) { this.availableSpace = availableSpace; }

    public String getSunlightLevel() { return sunlightLevel; }
    public void setSunlightLevel(String sunlightLevel) { this.sunlightLevel = sunlightLevel; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getHumidity() { return humidity; }
    public void setHumidity(String humidity) { this.humidity = humidity; }

    public String getIndoorOutdoor() { return indoorOutdoor; }
    public void setIndoorOutdoor(String indoorOutdoor) { this.indoorOutdoor = indoorOutdoor; }

    public String getRecommendations() { return recommendations; }
    public void setRecommendations(String recommendations) { this.recommendations = recommendations; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
