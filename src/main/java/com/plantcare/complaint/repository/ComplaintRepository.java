package com.plantcare.complaint.repository;

import com.plantcare.complaint.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, UUID> {
    List<Complaint> findByCustomerId(UUID customerId);
    List<Complaint> findByCompanyId(UUID companyId);
}
