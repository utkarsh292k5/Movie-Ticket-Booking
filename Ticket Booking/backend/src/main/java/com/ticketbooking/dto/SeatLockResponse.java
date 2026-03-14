package com.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SeatLockResponse {
    private boolean success;
    private String message;
    private String lockKey;
    private List<Long> lockedSeats;
    private int expirySeconds;
}

