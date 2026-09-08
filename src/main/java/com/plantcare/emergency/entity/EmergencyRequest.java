package com.plantcare.emergency.entity;

import com.plantcare.auth.entity.User;
import com.plantcare.company.entity.Company;
import com.plantcare.company.entity.CompanyLocation;
import com.plantcare.plantinstance.entity.PlantInstance;
import com.plantcare.worker.entity.WorkerProfile;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emergency_requests")
public class EmergencyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private CompanyLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_instance_id")
    private PlantInstance plantInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 30)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "photo_reference", length = 500)
    private String photoReference;

    @Column(name = "preferred_visit_date")
    private LocalDate preferredVisitDate;

    @Column(nullable = false, length = 30)
    private String status = "OPEN"; // OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_worker_id")
    private WorkerProfile assignedWorker;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public EmergencyRequest() {}

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

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public CompanyLocation getLocation() { return location; }
    public void setLocation(CompanyLocation location) { this.location = location; }

    public PlantInstance getPlantInstance() { return plantInstance; }
    public void setPlantInstance(PlantInstance plantInstance) { this.plantInstance = plantInstance; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getPhotoReference() { return photoReference; }
    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }

    public LocalDate getPreferredVisitDate() { return preferredVisitDate; }
    public void setPreferredVisitDate(LocalDate preferredVisitDate) { this.preferredVisitDate = preferredVisitDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public WorkerProfile getAssignedWorker() { return assignedWorker; }
    public void setAssignedWorker(WorkerProfile assignedWorker) { this.assignedWorker = assignedWorker; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
