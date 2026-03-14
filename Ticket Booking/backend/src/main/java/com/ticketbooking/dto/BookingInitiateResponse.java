package com.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BookingInitiateResponse {
    private Long bookingId;
    private String bookingCode;
    private BigDecimal totalAmount;
    private String paymentTransactionId;
}

