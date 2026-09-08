package com.plantcare.payment.dto;

import jakarta.validation.constraints.NotBlank;

public class VerifyPaymentRequest {

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotBlank(message = "Gateway reference is required")
    private String gatewayReference;

    public VerifyPaymentRequest() {}

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getGatewayReference() { return gatewayReference; }
    public void setGatewayReference(String gatewayReference) { this.gatewayReference = gatewayReference; }
}
