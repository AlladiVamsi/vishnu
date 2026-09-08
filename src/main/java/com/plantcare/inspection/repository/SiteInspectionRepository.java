package com.plantcare.inspection.repository;

import com.plantcare.inspection.entity.SiteInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SiteInspectionRepository extends JpaRepository<SiteInspection, UUID> {
    Optional<SiteInspection> findByBookingId(UUID bookingId);
    List<SiteInspection> findByCompanyId(UUID companyId);
}
