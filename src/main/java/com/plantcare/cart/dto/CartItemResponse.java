package com.plantcare.cart.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemResponse {

    private UUID id;
    private UUID plantId;
    private String plantName;
    private String plantImageUrl;
    private int quantity;
    private String orderType;
    private BigDecimal unitPrice;
    private BigDecimal installationCharge;
    private BigDecimal itemSubtotal;
    private UUID maintenancePlanId;
    private String maintenancePlanName;
    private BigDecimal maintenancePrice;

    public CartItemResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPlantId() { return plantId; }
    public void setPlantId(UUID plantId) { this.plantId = plantId; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getPlantImageUrl() { return plantImageUrl; }
    public void setPlantImageUrl(String plantImageUrl) { this.plantImageUrl = plantImageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getInstallationCharge() { return installationCharge; }
    public void setInstallationCharge(BigDecimal installationCharge) { this.installationCharge = installationCharge; }

    public BigDecimal getItemSubtotal() { return itemSubtotal; }
    public void setItemSubtotal(BigDecimal itemSubtotal) { this.itemSubtotal = itemSubtotal; }

    public UUID getMaintenancePlanId() { return maintenancePlanId; }
    public void setMaintenancePlanId(UUID maintenancePlanId) { this.maintenancePlanId = maintenancePlanId; }

    public String getMaintenancePlanName() { return maintenancePlanName; }
    public void setMaintenancePlanName(String maintenancePlanName) { this.maintenancePlanName = maintenancePlanName; }

    public BigDecimal getMaintenancePrice() { return maintenancePrice; }
    public void setMaintenancePrice(BigDecimal maintenancePrice) { this.maintenancePrice = maintenancePrice; }
}
