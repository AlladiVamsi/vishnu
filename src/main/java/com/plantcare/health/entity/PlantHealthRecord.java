package com.plantcare.health.entity;

import com.plantcare.plantinstance.entity.PlantInstance;
import com.plantcare.servicevisit.entity.ServiceVisit;
import com.plantcare.worker.entity.WorkerProfile;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "plant_health_records", indexes = {
        @Index(name = "idx_plant_health_instance", columnList = "plant_instance_id")
})
public class PlantHealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_instance_id", nullable = false)
    private PlantInstance plantInstance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_visit_id")
    private ServiceVisit serviceVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private WorkerProfile worker;

    @Column(name = "health_status", nullable = false, length = 30)
    private String healthStatus = "HEALTHY";

    @Column(length = 200)
    private String disease;

    @Column(length = 200)
    private String pest;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "photo_reference", length = 500)
    private String photoReference;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PlantHealthRecord() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public PlantInstance getPlantInstance() { return plantInstance; }
    public void setPlantInstance(PlantInstance plantInstance) { this.plantInstance = plantInstance; }

    public ServiceVisit getServiceVisit() { return serviceVisit; }
    public void setServiceVisit(ServiceVisit serviceVisit) { this.serviceVisit = serviceVisit; }

    public WorkerProfile getWorker() { return worker; }
    public void setWorker(WorkerProfile worker) { this.worker = worker; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getPest() { return pest; }
    public void setPest(String pest) { this.pest = pest; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPhotoReference() { return photoReference; }
    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
