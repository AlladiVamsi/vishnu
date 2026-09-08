package com.plantcare.company.repository;

import com.plantcare.company.entity.CompanyLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyLocationRepository extends JpaRepository<CompanyLocation, UUID> {
    List<CompanyLocation> findByCompanyId(UUID companyId);
}
