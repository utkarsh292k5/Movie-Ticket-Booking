package com.ticketbooking.controller;

import com.ticketbooking.entity.Payment;
import com.ticketbooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    /**
     * Process payment (mock)
     */
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(
            @RequestParam String transactionId,
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(paymentService.processPayment(transactionId, paymentMethod));
    }
    
    /**
     * Verify payment (webhook from payment gateway)
     */
    @PostMapping("/verify")
    public ResponseEntity<Payment> verifyPayment(
            @RequestParam String transactionId,
            @RequestParam String gatewayResponse) {
        return ResponseEntity.ok(paymentService.verifyPayment(transactionId, gatewayResponse));
    }
    
    /**
     * Get payment details
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<Payment> getPayment(@PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.getPaymentByTransactionId(transactionId));
    }
}

