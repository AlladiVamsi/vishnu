package com.plantcare.servicevisit.repository;

import com.plantcare.servicevisit.entity.ServiceVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceVisitRepository extends JpaRepository<ServiceVisit, UUID> {
    List<ServiceVisit> findBySubscriptionId(UUID subscriptionId);
    List<ServiceVisit> findByWorkerId(UUID workerId);
    List<ServiceVisit> findByScheduledDate(LocalDate date);
}
