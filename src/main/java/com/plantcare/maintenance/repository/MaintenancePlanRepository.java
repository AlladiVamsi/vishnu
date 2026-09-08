package com.plantcare.maintenance.repository;

import com.plantcare.maintenance.entity.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaintenancePlanRepository extends JpaRepository<MaintenancePlan, UUID> {
    Optional<MaintenancePlan> findByName(String name);
    List<MaintenancePlan> findByActiveTrue();
}
