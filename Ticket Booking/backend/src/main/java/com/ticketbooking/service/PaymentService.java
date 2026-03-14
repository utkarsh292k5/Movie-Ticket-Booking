package com.ticketbooking.service;

import com.ticketbooking.entity.Payment;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Payment service - mock implementation
 * In production, integrate with real payment gateway
 */
@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    /**
     * Mock payment processing
     * In production: integrate with Stripe, PayPal, Razorpay, etc.
     */
    public Payment processPayment(String transactionId, String paymentMethod) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        
        // Mock payment gateway call
        boolean paymentSuccess = mockPaymentGateway(payment.getAmount(), paymentMethod);
        
        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentMethod));
            payment.setPaymentGatewayResponse("SUCCESS");
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setPaymentGatewayResponse("FAILED");
        }
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Verify payment (webhook from payment gateway)
     */
    public Payment verifyPayment(String transactionId, String gatewayResponse) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        
        // In production: verify signature from payment gateway
        if (gatewayResponse.equals("SUCCESS")) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }
        
        payment.setPaymentGatewayResponse(gatewayResponse);
        return paymentRepository.save(payment);
    }
    
    public Payment getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }
    
    /**
     * Mock payment gateway - always succeeds in this demo
     * In production: call real payment gateway API
     */
    private boolean mockPaymentGateway(BigDecimal amount, String method) {
        // Simulate payment processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Mock: 95% success rate
        return Math.random() < 0.95;
    }
}

