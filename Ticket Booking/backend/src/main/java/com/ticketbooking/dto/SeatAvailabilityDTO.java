package com.ticketbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatAvailabilityDTO {
    private Long seatId;
    private String rowLabel;
    private Integer seatNumber;
    private String seatType;
    private String status; // AVAILABLE, LOCKED, BOOKED
    private String price;
}

