package com.plantcare.booking.service;

import com.plantcare.auth.entity.User;
import com.plantcare.auth.repository.UserRepository;
import com.plantcare.booking.dto.BookingItemResponse;
import com.plantcare.booking.dto.BookingResponse;
import com.plantcare.booking.dto.CreateBookingRequest;
import com.plantcare.booking.entity.Booking;
import com.plantcare.booking.entity.BookingItem;
import com.plantcare.booking.entity.BookingStatus;
import com.plantcare.booking.repository.BookingRepository;
import com.plantcare.cart.dto.CartResponse;
import com.plantcare.cart.entity.Cart;
import com.plantcare.cart.entity.CartItem;
import com.plantcare.cart.repository.CartRepository;
import com.plantcare.cart.service.CartService;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.common.exception.ValidationException;
import com.plantcare.company.entity.CompanyLocation;
import com.plantcare.company.repository.CompanyLocationRepository;
import com.plantcare.inventory.service.InventoryService;
import com.plantcare.maintenance.entity.MaintenancePlan;
import com.plantcare.maintenance.repository.MaintenancePlanRepository;
import com.plantcare.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final CompanyLocationRepository locationRepository;
    private final MaintenancePlanRepository maintenancePlanRepository;
    private final InventoryService inventoryService;

    public BookingService(BookingRepository bookingRepository,
                          CartRepository cartRepository,
                          CartService cartService,
                          UserRepository userRepository,
                          CompanyLocationRepository locationRepository,
                          MaintenancePlanRepository maintenancePlanRepository,
                          InventoryService inventoryService) {
        this.bookingRepository = bookingRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.maintenancePlanRepository = maintenancePlanRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public BookingResponse createBookingFromCart(UUID customerId, CreateBookingRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", customerId));

        CompanyLocation location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("CompanyLocation", "id", request.getLocationId()));

        SecurityUtils.verifyCompanyOwnership(location.getCompany().getId());

        Cart cart = cartRepository.findByUserId(customerId)
                .orElseThrow(() -> new ValidationException("Cart is empty. Please add items to cart before booking."));

        if (cart.getItems().isEmpty()) {
            throw new ValidationException("Cart is empty. Please add items to cart before booking.");
        }

        CartResponse cartTotals = cartService.calculateCartTotals(cart);

        MaintenancePlan maintenancePlan = null;
        if (request.getMaintenancePlanId() != null) {
            maintenancePlan = maintenancePlanRepository.findById(request.getMaintenancePlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("MaintenancePlan", "id", request.getMaintenancePlanId()));
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setCompany(location.getCompany());
        booking.setLocation(location);
        booking.setPreferredInstallationDate(request.getPreferredInstallationDate());
        booking.setSiteInspectionRequired(request.isSiteInspectionRequired());
        booking.setMaintenancePlan(maintenancePlan);
        booking.setSubtotal(cartTotals.getSubtotal());
        booking.setInstallationCharges(cartTotals.getInstallationCharges());
        booking.setMaintenanceCharges(cartTotals.getMaintenanceCharges());
        booking.setTax(cartTotals.getTax());
        booking.setDiscount(cartTotals.getDiscount());
        booking.setTotalAmount(cartTotals.getTotalAmount());
        booking.setStatus(BookingStatus.CREATED);

        List<BookingItem> bookingItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            inventoryService.reserveStock(cartItem.getPlant().getId(), cartItem.getQuantity());

            BookingItem bookingItem = new BookingItem();
            bookingItem.setBooking(booking);
            bookingItem.setPlant(cartItem.getPlant());
            bookingItem.setQuantity(cartItem.getQuantity());
            bookingItem.setOrderType(cartItem.getOrderType());
            bookingItem.setUnitPrice(cartItem.getUnitPrice());
            bookingItem.setTotalPrice(cartItem.getUnitPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())));

            bookingItems.add(bookingItem);
        }
        booking.setItems(bookingItems);

        booking = bookingRepository.save(booking);

        cartService.clearCart(customerId);

        return mapToResponse(booking);
    }

    public List<BookingResponse> getCustomerBookings(UUID customerId) {
        return bookingRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
        SecurityUtils.verifyCompanyOwnership(booking.getCompany().getId());
        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse updateBookingStatus(UUID bookingId, BookingStatus nextStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
        SecurityUtils.verifyCompanyOwnership(booking.getCompany().getId());

        booking.setStatus(nextStatus);

        if (nextStatus == BookingStatus.CANCELLED) {
            for (BookingItem item : booking.getItems()) {
                inventoryService.releaseStock(item.getPlant().getId(), item.getQuantity());
            }
        }

        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setCustomerId(booking.getCustomer().getId());
        response.setCustomerName(booking.getCustomer().getFirstName() + " " + (booking.getCustomer().getLastName() != null ? booking.getCustomer().getLastName() : ""));
        response.setCompanyId(booking.getCompany().getId());
        response.setCompanyName(booking.getCompany().getCompanyName());
        response.setLocationId(booking.getLocation().getId());
        response.setLocationName(booking.getLocation().getLocationName());
        response.setBookingDate(booking.getBookingDate());
        response.setPreferredInstallationDate(booking.getPreferredInstallationDate());
        response.setBookingType(booking.getBookingType());
        response.setSiteInspectionRequired(booking.isSiteInspectionRequired());
        if (booking.getMaintenancePlan() != null) {
            response.setMaintenancePlanId(booking.getMaintenancePlan().getId());
            response.setMaintenancePlanName(booking.getMaintenancePlan().getName());
        }
        response.setSubtotal(booking.getSubtotal());
        response.setInstallationCharges(booking.getInstallationCharges());
        response.setMaintenanceCharges(booking.getMaintenanceCharges());
        response.setTax(booking.getTax());
        response.setDiscount(booking.getDiscount());
        response.setTotalAmount(booking.getTotalAmount());
        response.setPaymentStatus(booking.getPaymentStatus());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());

        List<BookingItemResponse> itemResponses = booking.getItems().stream().map(item -> {
            BookingItemResponse itemResp = new BookingItemResponse();
            itemResp.setId(item.getId());
            itemResp.setPlantId(item.getPlant().getId());
            itemResp.setPlantName(item.getPlant().getName());
            itemResp.setQuantity(item.getQuantity());
            itemResp.setOrderType(item.getOrderType());
            itemResp.setUnitPrice(item.getUnitPrice());
            itemResp.setTotalPrice(item.getTotalPrice());
            return itemResp;
        }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}
