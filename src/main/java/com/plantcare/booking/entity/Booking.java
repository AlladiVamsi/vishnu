package com.plantcare.booking.entity;

import com.plantcare.auth.entity.User;
import com.plantcare.company.entity.Company;
import com.plantcare.company.entity.CompanyLocation;
import com.plantcare.maintenance.entity.MaintenancePlan;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_bookings_customer", columnList = "customer_id"),
        @Index(name = "idx_bookings_company", columnList = "company_id"),
        @Index(name = "idx_bookings_status", columnList = "status")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private CompanyLocation location;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(name = "preferred_installation_date")
    private LocalDateTime preferredInstallationDate;

    @Column(name = "booking_type", nullable = false, length = 30)
    private String bookingType = "PURCHASE";

    @Column(name = "site_inspection_required", nullable = false)
    private boolean siteInspectionRequired = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_plan_id")
    private MaintenancePlan maintenancePlan;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "installation_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal installationCharges = BigDecimal.ZERO;

    @Column(name = "maintenance_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal maintenanceCharges = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "PENDING";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private BookingStatus status = BookingStatus.CREATED;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Booking() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (bookingDate == null) bookingDate = now;
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public CompanyLocation getLocation() { return location; }
    public void setLocation(CompanyLocation location) { this.location = location; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public LocalDateTime getPreferredInstallationDate() { return preferredInstallationDate; }
    public void setPreferredInstallationDate(LocalDateTime preferredInstallationDate) { this.preferredInstallationDate = preferredInstallationDate; }

    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }

    public boolean isSiteInspectionRequired() { return siteInspectionRequired; }
    public void setSiteInspectionRequired(boolean siteInspectionRequired) { this.siteInspectionRequired = siteInspectionRequired; }

    public MaintenancePlan getMaintenancePlan() { return maintenancePlan; }
    public void setMaintenancePlan(MaintenancePlan maintenancePlan) { this.maintenancePlan = maintenancePlan; }

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
    public void setStatus(BookingStatus status) {
        if (this.status != null) {
            this.status.validateTransitionTo(status);
        }
        this.status = status;
    }

    public List<BookingItem> getItems() { return items; }
    public void setItems(List<BookingItem> items) { this.items = items; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
