package com.plantcare.plantinstance.service;

import com.plantcare.booking.entity.Booking;
import com.plantcare.booking.entity.BookingItem;
import com.plantcare.booking.entity.BookingStatus;
import com.plantcare.booking.repository.BookingRepository;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.inventory.service.InventoryService;
import com.plantcare.plantinstance.dto.PlantInstanceResponse;
import com.plantcare.plantinstance.entity.PlantInstance;
import com.plantcare.plantinstance.repository.PlantInstanceRepository;
import com.plantcare.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PlantInstanceService {

    private static final AtomicLong COUNTER = new AtomicLong(1000);

    private final PlantInstanceRepository instanceRepository;
    private final BookingRepository bookingRepository;
    private final InventoryService inventoryService;

    public PlantInstanceService(PlantInstanceRepository instanceRepository,
                                BookingRepository bookingRepository,
                                InventoryService inventoryService) {
        this.instanceRepository = instanceRepository;
        this.bookingRepository = bookingRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public List<PlantInstanceResponse> registerInstancesForBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        List<PlantInstance> createdInstances = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (BookingItem item : booking.getItems()) {
            boolean isRental = "RENTAL".equalsIgnoreCase(item.getOrderType());
            inventoryService.confirmInstallationStock(item.getPlant().getId(), item.getQuantity(), isRental);

            for (int i = 0; i < item.getQuantity(); i++) {
                String uniqueCode = String.format("PLANT-%d-%06d", today.getYear(), COUNTER.incrementAndGet());
                String qrCode = "QR_" + uniqueCode;

                PlantInstance instance = new PlantInstance();
                instance.setPlant(item.getPlant());
                instance.setBooking(booking);
                instance.setCompany(booking.getCompany());
                instance.setLocation(booking.getLocation());
                instance.setUniquePlantCode(uniqueCode);
                instance.setQrCode(qrCode);
                instance.setInstallationDate(today);
                instance.setCurrentHealthStatus("HEALTHY");
                instance.setActive(true);

                createdInstances.add(instanceRepository.save(instance));
            }
        }

        booking.setStatus(BookingStatus.INSTALLATION_COMPLETED);
        bookingRepository.save(booking);

        return createdInstances.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public PlantInstanceResponse getByQrCode(String qrCode) {
        PlantInstance instance = instanceRepository.findByQrCode(qrCode)
                .or(() -> instanceRepository.findByUniquePlantCode(qrCode))
                .orElseThrow(() -> new ResourceNotFoundException("PlantInstance", "qrCode/uniqueCode", qrCode));
        return mapToResponse(instance);
    }

    public PlantInstanceResponse getById(UUID id) {
        PlantInstance instance = instanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlantInstance", "id", id));
        SecurityUtils.verifyCompanyOwnership(instance.getCompany().getId());
        return mapToResponse(instance);
    }

    public List<PlantInstanceResponse> getCompanyInstances(UUID companyId) {
        SecurityUtils.verifyCompanyOwnership(companyId);
        return instanceRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PlantInstanceResponse mapToResponse(PlantInstance instance) {
        PlantInstanceResponse response = new PlantInstanceResponse();
        response.setId(instance.getId());
        response.setPlantId(instance.getPlant().getId());
        response.setPlantName(instance.getPlant().getName());
        response.setBookingId(instance.getBooking().getId());
        response.setCompanyId(instance.getCompany().getId());
        response.setCompanyName(instance.getCompany().getCompanyName());
        response.setLocationId(instance.getLocation().getId());
        response.setLocationName(instance.getLocation().getLocationName());
        response.setUniquePlantCode(instance.getUniquePlantCode());
        response.setQrCode(instance.getQrCode());
        response.setInstallationDate(instance.getInstallationDate());
        response.setCurrentHealthStatus(instance.getCurrentHealthStatus());
        response.setActive(instance.isActive());
        return response;
    }
}
