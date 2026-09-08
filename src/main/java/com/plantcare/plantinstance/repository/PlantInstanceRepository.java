package com.plantcare.plantinstance.repository;

import com.plantcare.plantinstance.entity.PlantInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlantInstanceRepository extends JpaRepository<PlantInstance, UUID> {
    Optional<PlantInstance> findByUniquePlantCode(String uniquePlantCode);
    Optional<PlantInstance> findByQrCode(String qrCode);
    List<PlantInstance> findByCompanyId(UUID companyId);
    List<PlantInstance> findByBookingId(UUID bookingId);
}
