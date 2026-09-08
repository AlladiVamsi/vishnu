package com.plantcare.plant.repository;

import com.plantcare.plant.entity.PlantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlantImageRepository extends JpaRepository<PlantImage, UUID> {
    List<PlantImage> findByPlantId(UUID plantId);
}
