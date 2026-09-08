package com.plantcare.booking.dto;

import com.plantcare.booking.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BookingResponse {

    private UUID id;
    private UUID customerId;
    private String customerName;
    private UUID companyId;
    private String companyName;
    private UUID locationId;
    private String locationName;
    private LocalDateTime bookingDate;
    private LocalDateTime preferredInstallationDate;
    private String bookingType;
    private boolean siteInspectionRequired;
    private UUID maintenancePlanId;
    private String maintenancePlanName;
    private BigDecimal subtotal;
    private BigDecimal installationCharges;
    private BigDecimal maintenanceCharges;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private String paymentStatus;
    private BookingStatus status;
    private List<BookingItemResponse> items;
    private LocalDateTime createdAt;

    public BookingResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public UUID getCompanyId() { return companyId; }
    public void setCompanyId(UUID companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }

    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalDateTime getPreferredInstallationDate() { return preferredInstallationDate; }
    public void setPreferredInstallationDate(LocalDateTime preferredInstallationDate) { this.preferredInstallationDate = preferredInstallationDate; }

    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }

    public boolean isSiteInspectionRequired() { return siteInspectionRequired; }
    public void setSiteInspectionRequired(boolean siteInspectionRequired) { this.siteInspectionRequired = siteInspectionRequired; }

    public UUID getMaintenancePlanId() { return maintenancePlanId; }
    public void setMaintenancePlanId(UUID maintenancePlanId) { this.maintenancePlanId = maintenancePlanId; }

    public String getMaintenancePlanName() { return maintenancePlanName; }
    public void setMaintenancePlanName(String maintenancePlanName) { this.maintenancePlanName = maintenancePlanName; }

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

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public List<BookingItemResponse> getItems() { return items; }
    public void setItems(List<BookingItemResponse> items) { this.items = items; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
