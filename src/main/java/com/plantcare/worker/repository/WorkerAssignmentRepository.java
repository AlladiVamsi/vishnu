package com.plantcare.worker.repository;

import com.plantcare.worker.entity.WorkerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkerAssignmentRepository extends JpaRepository<WorkerAssignment, UUID> {
    List<WorkerAssignment> findByWorkerId(UUID workerId);
    List<WorkerAssignment> findByBookingId(UUID bookingId);
}
