package com.plantcare.plant.repository;

import com.plantcare.plant.entity.PlantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlantCategoryRepository extends JpaRepository<PlantCategory, UUID> {
    Optional<PlantCategory> findByName(String name);
    boolean existsByName(String name);
    List<PlantCategory> findByActiveTrue();
}
