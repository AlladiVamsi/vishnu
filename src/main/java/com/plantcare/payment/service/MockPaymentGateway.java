package com.plantcare.payment.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentTransaction initiatePayment(UUID bookingId, BigDecimal amount, String currency, String paymentMethod) {
        String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String gatewayRef = "MOCK_REF_" + System.currentTimeMillis();
        return new PaymentTransaction(transactionId, gatewayRef, "PENDING");
    }

    @Override
    public boolean verifyPayment(String transactionId, String gatewayReference) {
        // Mock gateway verification: returns true for valid non-null transaction reference
        return transactionId != null && !transactionId.isBlank();
    }
}
