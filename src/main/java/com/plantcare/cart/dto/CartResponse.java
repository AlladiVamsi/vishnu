package com.plantcare.cart.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CartResponse {

    private UUID id;
    private List<CartItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal installationCharges;
    private BigDecimal maintenanceCharges;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal totalAmount;

    public CartResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getInstallationCharges() { return installationCharges; }
    public void setInstallationCharges(BigDecimal installationCharges) { this.installationCharges = installationCharges; }

    public BigDecimal getMaintenanceCharges() { return maintenanceCharges; }
    public void setMaintenanceCharges(BigDecimal maintenanceCharges) { this.maintenanceCharges = maintenanceCharges; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
