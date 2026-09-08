package com.plantcare.inventory.entity;

import com.plantcare.plant.entity.Plant;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventories")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false, unique = true)
    private Plant plant;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity = 0;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity = 0;

    @Column(name = "installed_quantity", nullable = false)
    private int installedQuantity = 0;

    @Column(name = "rental_quantity", nullable = false)
    private int rentalQuantity = 0;

    @Column(name = "damaged_quantity", nullable = false)
    private int damagedQuantity = 0;

    @Version
    private Long version;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Inventory() {}

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Plant getPlant() { return plant; }
    public void setPlant(Plant plant) { this.plant = plant; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public int getInstalledQuantity() { return installedQuantity; }
    public void setInstalledQuantity(int installedQuantity) { this.installedQuantity = installedQuantity; }

    public int getRentalQuantity() { return rentalQuantity; }
    public void setRentalQuantity(int rentalQuantity) { this.rentalQuantity = rentalQuantity; }

    public int getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(int damagedQuantity) { this.damagedQuantity = damagedQuantity; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
