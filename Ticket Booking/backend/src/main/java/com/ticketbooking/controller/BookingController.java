package com.ticketbooking.controller;

import com.ticketbooking.dto.BookingInitiateRequest;
import com.ticketbooking.dto.BookingInitiateResponse;
import com.ticketbooking.dto.SeatLockRequest;
import com.ticketbooking.dto.SeatLockResponse;
import com.ticketbooking.entity.Booking;
import com.ticketbooking.service.BookingService;
import com.ticketbooking.service.SeatLockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatLockService seatLockService;

    /**
     * Lock seats before booking (5 min TTL)
     */
    @PostMapping("/shows/{showId}/lock-seats")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SeatLockResponse> lockSeats(@Valid @RequestBody SeatLockRequest request) {
        try {
            String lockKey = seatLockService.lockSeats(request.getShowId(), request.getSeatIds(), request.getUserId());
            return ResponseEntity.ok(new SeatLockResponse(
                    true,
                    "Seats locked successfully",
                    lockKey,
                    request.getSeatIds(),
                    300
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SeatLockResponse(false, e.getMessage(), null, null, 0));
        }
    }

    /**
     * Unlock seats manually
     */
    @PostMapping("/shows/{showId}/unlock-seats")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> unlockSeats(@PathVariable Long showId, @RequestBody List<Long> seatIds) {
        seatLockService.unlockSeats(showId, seatIds);
        return ResponseEntity.ok().build();
    }

    /**
     * Initiate booking (create pending booking + lock seats)
     */
    @PostMapping("/bookings/initiate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BookingInitiateResponse> initiateBooking(@Valid @RequestBody BookingInitiateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.initiateBooking(request));
    }

    /**
     * Confirm booking after payment success
     */
    @PostMapping("/bookings/confirm")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Booking> confirmBooking(
            @RequestParam Long bookingId,
            @RequestParam String transactionId) {
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId, transactionId));
    }

    /**
     * Get booking by ID
     */
    @GetMapping("/bookings/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    /**
     * Get user's bookings
     */
    @GetMapping("/users/me/bookings")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Booking>> getUserBookings(@RequestParam Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    /**
     * Cancel booking
     */
    @PostMapping("/bookings/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    /**
     * Admin: Get all bookings for a show
     */
    @GetMapping("/admin/shows/{showId}/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getShowBookings(@PathVariable Long showId) {
        return ResponseEntity.ok(bookingService.getShowBookings(showId));
    }

    /**
     * Admin: Get booking statistics
     */
    @GetMapping("/admin/bookings/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.Map<String, Object>> getBookingStats() {
        return ResponseEntity.ok(bookingService.getBookingStats());
    }
}