package com.plantcare.worker.repository;

import com.plantcare.worker.entity.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, UUID> {
    Optional<WorkerProfile> findByUserId(UUID userId);
    Optional<WorkerProfile> findByEmployeeCode(String employeeCode);
}
