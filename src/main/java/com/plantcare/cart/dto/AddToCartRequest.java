package com.plantcare.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class AddToCartRequest {

    @NotNull(message = "Plant ID is required")
    private UUID plantId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity = 1;

    private UUID userId;
    private String orderType = "PURCHASE"; // PURCHASE, RENTAL
    private UUID maintenancePlanId;

    public AddToCartRequest() {}

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getPlantId() { return plantId; }
    public void setPlantId(UUID plantId) { this.plantId = plantId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public UUID getMaintenancePlanId() { return maintenancePlanId; }
    public void setMaintenancePlanId(UUID maintenancePlanId) { this.maintenancePlanId = maintenancePlanId; }
}
