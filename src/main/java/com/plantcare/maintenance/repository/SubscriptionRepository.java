package com.plantcare.maintenance.repository;

import com.plantcare.maintenance.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByBookingId(UUID bookingId);
    List<Subscription> findByCompanyId(UUID companyId);
    List<Subscription> findByStatusAndNextServiceDateLessThanEqual(String status, LocalDate date);
}
