package com.plantcare.invoice.service;

import com.plantcare.booking.entity.Booking;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.invoice.entity.Invoice;
import com.plantcare.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Invoice generateInvoiceForBooking(Booking booking) {
        String invoiceNumber = "INV-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setBooking(booking);
        invoice.setCompany(booking.getCompany());
        invoice.setSubtotal(booking.getSubtotal());
        invoice.setInstallationCharges(booking.getInstallationCharges());
        invoice.setMaintenanceCharges(booking.getMaintenanceCharges());
        invoice.setTax(booking.getTax());
        invoice.setDiscount(booking.getDiscount());
        invoice.setTotalAmount(booking.getTotalAmount());
        invoice.setPaymentStatus("PAID");

        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceByBookingId(UUID bookingId) {
        return invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice for booking", "bookingId", bookingId));
    }
}
