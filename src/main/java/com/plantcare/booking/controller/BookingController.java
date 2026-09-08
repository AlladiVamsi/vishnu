package com.plantcare.booking.controller;

import com.plantcare.booking.dto.BookingResponse;
import com.plantcare.booking.dto.CreateBookingRequest;
import com.plantcare.booking.entity.BookingStatus;
import com.plantcare.booking.service.BookingService;
import com.plantcare.common.response.ApiResponse;
import com.plantcare.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking Management", description = "Plant Purchase & Rental Booking REST APIs")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Checkout cart and create booking with stock reservation")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            @RequestParam(required = false) UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
        UUID effectiveUserId = resolveUserId(userId, headerUserId, request.getUserId());
        BookingResponse booking = bookingService.createBookingFromCart(effectiveUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully", booking));
    }

    @GetMapping
    @Operation(summary = "Get bookings for currently logged-in customer/company")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getMyBookings(
            @RequestParam(required = false) UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
        UUID effectiveUserId = resolveUserId(userId, headerUserId, null);
        List<BookingResponse> bookings = bookingService.getCustomerBookings(effectiveUserId);
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

    private UUID resolveUserId(UUID paramUserId, UUID headerUserId, UUID bodyUserId) {
        if (bodyUserId != null) return bodyUserId;
        if (paramUserId != null) return paramUserId;
        if (headerUserId != null) return headerUserId;
        UUID secId = SecurityUtils.getCurrentUserId();
        if (secId != null) return secId;
        throw new com.plantcare.common.exception.ValidationException("User ID is required (pass via userId param/body or X-User-Id header)");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detailed booking info by ID")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable UUID id) {
        BookingResponse booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel booking and release reserved stock")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable UUID id) {
        BookingResponse booking = bookingService.updateBookingStatus(id, BookingStatus.CANCELLED);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled and stock released", booking));
    }
}
