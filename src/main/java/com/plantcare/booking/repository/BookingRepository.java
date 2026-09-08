package com.plantcare.booking.repository;

import com.plantcare.booking.entity.Booking;
import com.plantcare.booking.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByCustomerId(UUID customerId);
    List<Booking> findByCompanyId(UUID companyId);
    Page<Booking> findByCompanyId(UUID companyId, Pageable pageable);
    List<Booking> findByStatus(BookingStatus status);
}
