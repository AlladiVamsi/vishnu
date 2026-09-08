package com.plantcare.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreatePaymentRequest {

    @NotNull(message = "Booking ID is required")
    private UUID bookingId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // CARD, UPI, NETBANKING

    public CreatePaymentRequest() {}

    public UUID getBookingId() { return bookingId; }
    public void setBookingId(UUID bookingId) { this.bookingId = bookingId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
