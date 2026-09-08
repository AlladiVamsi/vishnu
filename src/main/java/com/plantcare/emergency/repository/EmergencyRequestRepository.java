package com.plantcare.emergency.repository;

import com.plantcare.emergency.entity.EmergencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, UUID> {
    List<EmergencyRequest> findByCompanyId(UUID companyId);
    List<EmergencyRequest> findByCustomerId(UUID customerId);
}
