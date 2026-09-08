package com.plantcare.health.repository;

import com.plantcare.health.entity.PlantHealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlantHealthRecordRepository extends JpaRepository<PlantHealthRecord, UUID> {
    List<PlantHealthRecord> findByPlantInstanceIdOrderByCreatedAtDesc(UUID plantInstanceId);
}
