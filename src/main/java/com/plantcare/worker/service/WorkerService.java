package com.plantcare.worker.service;

import com.plantcare.auth.entity.User;
import com.plantcare.auth.repository.UserRepository;
import com.plantcare.booking.entity.Booking;
import com.plantcare.booking.entity.BookingStatus;
import com.plantcare.booking.repository.BookingRepository;
import com.plantcare.common.exception.ForbiddenException;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.security.SecurityUtils;
import com.plantcare.worker.dto.AssignWorkerRequest;
import com.plantcare.worker.dto.WorkerResponse;
import com.plantcare.worker.entity.WorkerAssignment;
import com.plantcare.worker.entity.WorkerProfile;
import com.plantcare.worker.repository.WorkerAssignmentRepository;
import com.plantcare.worker.repository.WorkerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final WorkerProfileRepository workerRepository;
    private final WorkerAssignmentRepository assignmentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public WorkerService(WorkerProfileRepository workerRepository,
                         WorkerAssignmentRepository assignmentRepository,
                         BookingRepository bookingRepository,
                         UserRepository userRepository) {
        this.workerRepository = workerRepository;
        this.assignmentRepository = assignmentRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public List<WorkerResponse> getAllWorkers() {
        return workerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WorkerResponse getWorkerById(UUID id) {
        WorkerProfile worker = workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkerProfile", "id", id));
        return mapToResponse(worker);
    }

    public WorkerProfile getCurrentWorkerProfile() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        return workerRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ForbiddenException("Current authenticated user is not registered as a service worker"));
    }

    public void verifyWorkerAssignedToBooking(UUID bookingId) {
        if (SecurityUtils.hasRole("ADMIN")) return; // Admins bypass worker check

        WorkerProfile currentWorker = getCurrentWorkerProfile();
        List<WorkerAssignment> assignments = assignmentRepository.findByBookingId(bookingId);
        boolean isAssigned = assignments.stream()
                .anyMatch(a -> a.getWorker().getId().equals(currentWorker.getId()));

        if (!isAssigned) {
            throw new ForbiddenException("Access Denied: You are not assigned to perform service on this booking job");
        }
    }

    @Transactional
    public WorkerAssignment assignWorkerToBooking(UUID bookingId, AssignWorkerRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        WorkerProfile worker = workerRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("WorkerProfile", "id", request.getWorkerId()));

        UUID currentUserId = SecurityUtils.getCurrentUserId();
        User assignedByUser = currentUserId != null ? userRepository.findById(currentUserId).orElse(null) : null;

        WorkerAssignment assignment = new WorkerAssignment();
        assignment.setBooking(booking);
        assignment.setWorker(worker);
        assignment.setAssignedBy(assignedByUser);
        assignment.setNotes(request.getNotes());
        assignment.setStatus("ASSIGNED");

        assignment = assignmentRepository.save(assignment);

        worker.setAvailabilityStatus("BUSY");
        workerRepository.save(worker);

        booking.setStatus(BookingStatus.WORKER_ASSIGNED);
        bookingRepository.save(booking);

        return assignment;
    }

    @Transactional
    public WorkerResponse createWorker(com.plantcare.worker.dto.CreateWorkerRequest request) {
        User user;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));
        } else {
            // Auto create worker user account if not provided
            user = new User();
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setPasswordHash("$2a$10$wE9V8w7V8w7V8w7V8w7V8u"); // dummy hashed
            user.setFirstName(request.getName());
            user.setLastName("Worker");
            user.setRole(com.plantcare.auth.entity.Role.WORKER);
            user.setStatus(com.plantcare.auth.entity.UserStatus.ACTIVE);
            user = userRepository.save(user);
        }

        WorkerProfile profile = new WorkerProfile();
        profile.setUser(user);
        profile.setEmployeeCode("WRK-" + System.currentTimeMillis() % 1000000);
        profile.setName(request.getName());
        profile.setPhone(request.getPhone());
        profile.setEmail(request.getEmail());
        profile.setExperience(request.getExperience());
        profile.setSpecialization(request.getSpecialization());
        profile.setActive(true);
        profile.setAvailabilityStatus("AVAILABLE");

        profile = workerRepository.save(profile);
        return mapToResponse(profile);
    }

    private WorkerResponse mapToResponse(WorkerProfile worker) {
        WorkerResponse response = new WorkerResponse();
        response.setId(worker.getId());
        response.setUserId(worker.getUser().getId());
        response.setEmployeeCode(worker.getEmployeeCode());
        response.setName(worker.getName());
        response.setPhone(worker.getPhone());
        response.setEmail(worker.getEmail());
        response.setExperience(worker.getExperience());
        response.setSpecialization(worker.getSpecialization());
        response.setActive(worker.isActive());
        response.setAvailabilityStatus(worker.getAvailabilityStatus());
        return response;
    }
}
