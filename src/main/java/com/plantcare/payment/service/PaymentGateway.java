package com.plantcare.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGateway {
    PaymentTransaction initiatePayment(UUID bookingId, BigDecimal amount, String currency, String paymentMethod);
    boolean verifyPayment(String transactionId, String gatewayReference);

    class PaymentTransaction {
        private String transactionId;
        private String gatewayReference;
        private String status;

        public PaymentTransaction(String transactionId, String gatewayReference, String status) {
            this.transactionId = transactionId;
            this.gatewayReference = gatewayReference;
            this.status = status;
        }

        public String getTransactionId() { return transactionId; }
        public String getGatewayReference() { return gatewayReference; }
        public String getStatus() { return status; }
    }
}
