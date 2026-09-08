package com.plantcare.payment.service;

import com.plantcare.booking.entity.Booking;
import com.plantcare.booking.entity.BookingStatus;
import com.plantcare.booking.repository.BookingRepository;
import com.plantcare.common.exception.PaymentException;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.invoice.entity.Invoice;
import com.plantcare.invoice.service.InvoiceService;
import com.plantcare.payment.dto.CreatePaymentRequest;
import com.plantcare.payment.dto.PaymentResponse;
import com.plantcare.payment.dto.VerifyPaymentRequest;
import com.plantcare.payment.entity.Payment;
import com.plantcare.payment.repository.PaymentRepository;
import com.plantcare.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentGateway paymentGateway;
    private final InvoiceService invoiceService;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          PaymentGateway paymentGateway,
                          InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.paymentGateway = paymentGateway;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public PaymentResponse initiatePayment(CreatePaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", request.getBookingId()));

        SecurityUtils.verifyCompanyOwnership(booking.getCompany().getId());

        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        bookingRepository.save(booking);

        PaymentGateway.PaymentTransaction transaction = paymentGateway.initiatePayment(
                booking.getId(), booking.getTotalAmount(), "INR", request.getPaymentMethod()
        );

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTransactionId(transaction.getTransactionId());
        payment.setAmount(booking.getTotalAmount());
        payment.setCurrency("INR");
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus("PENDING");
        payment.setGatewayReference(transaction.getGatewayReference());

        payment = paymentRepository.save(payment);
        return mapToResponse(payment);
    }

    @Transactional
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {
        Payment payment = paymentRepository.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment transaction", "transactionId", request.getTransactionId()));

        Booking booking = payment.getBooking();
        SecurityUtils.verifyCompanyOwnership(booking.getCompany().getId());

        boolean verified = paymentGateway.verifyPayment(request.getTransactionId(), request.getGatewayReference());

        if (!verified) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new PaymentException("Payment verification failed at gateway");
        }

        payment.setStatus("SUCCESS");
        payment.setGatewayReference(request.getGatewayReference());
        payment = paymentRepository.save(payment);

        booking.setPaymentStatus("COMPLETED");
        booking.setStatus(BookingStatus.PAYMENT_COMPLETED);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        Invoice invoice = invoiceService.generateInvoiceForBooking(booking);
        payment.setInvoiceId(invoice.getId());
        paymentRepository.save(payment);

        return mapToResponse(payment);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setBookingId(payment.getBooking().getId());
        response.setTransactionId(payment.getTransactionId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());
        response.setGatewayReference(payment.getGatewayReference());
        return response;
    }
}
