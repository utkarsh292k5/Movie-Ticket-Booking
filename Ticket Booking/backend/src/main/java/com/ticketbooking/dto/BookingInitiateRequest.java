package com.ticketbooking.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingInitiateRequest {
    @NotNull(message = "Show ID is required")
    private Long showId;
    
    @NotEmpty(message = "Seat IDs are required")
    private List<Long> seatIds;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Idempotency key is required")
    private String idempotencyKey;
}

