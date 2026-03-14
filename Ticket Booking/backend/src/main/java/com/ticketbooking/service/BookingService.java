package com.ticketbooking.service;

import com.ticketbooking.dto.BookingInitiateRequest;
import com.ticketbooking.dto.BookingInitiateResponse;
import com.ticketbooking.entity.*;
import com.ticketbooking.exception.BookingException;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CRITICAL SERVICE: Handles booking workflow with concurrency safety
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingSeatRepository bookingSeatRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SeatLockService seatLockService;

    /**
     * STEP 1: Initiate booking
     * - Check idempotency
     * - Verify seats are available
     * - Lock seats in Redis
     * - Create pending booking
     * - Create payment record
     */
    @Transactional
    public BookingInitiateResponse initiateBooking(BookingInitiateRequest request) {
        // Idempotency check - prevent duplicate bookings
        if (bookingRepository.findByIdempotencyKey(request.getIdempotencyKey()).isPresent()) {
            throw new BookingException("Duplicate booking request");
        }

        // Validate show
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        if (!show.getActive() || show.getStatus() != Show.ShowStatus.SCHEDULED) {
            throw new BookingException("Show is not available for booking");
        }

        // Validate user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate seats exist and are active
        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new BookingException("One or more seats not found");
        }

        // Check if seats are already booked in database
        for (Long seatId : request.getSeatIds()) {
            if (bookingSeatRepository.existsByShowIdAndSeatId(show.getId(), seatId)) {
                throw new BookingException("Seat " + seatId + " is already booked");
            }
        }

        // Lock seats in Redis (throws SeatLockedException if already locked)
        String lockKey = seatLockService.lockSeats(show.getId(), request.getSeatIds(), user.getId());

        // Calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (Seat seat : seats) {
            BigDecimal price = getPriceForSeat(show, seat);
            totalAmount = totalAmount.add(price);
        }

        // Create booking record
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingCode(generateBookingCode());
        booking.setNumberOfSeats(seats.size());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(Booking.BookingStatus.INITIATED);
        booking.setIdempotencyKey(request.getIdempotencyKey());

        booking = bookingRepository.save(booking);

        // Create booking seats
        for (Seat seat : seats) {
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(booking);
            bookingSeat.setShow(show);
            bookingSeat.setSeat(seat);
            bookingSeat.setPrice(getPriceForSeat(show, seat));
            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);
        booking.setBookingSeats(bookingSeats);

        // Create payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setAmount(totalAmount);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(payment);

        return new BookingInitiateResponse(
                booking.getId(),
                booking.getBookingCode(),
                totalAmount,
                payment.getTransactionId()
        );
    }

    /**
     * STEP 2: Confirm booking after successful payment
     * - Verify payment
     * - Verify lock ownership
     * - Mark booking as CONFIRMED
     * - Update show availability
     * - Release Redis locks (seats now in DB)
     */
    @Transactional
    public Booking confirmBooking(Long bookingId, String transactionId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CONFIRMED) {
            return booking; // Already confirmed (idempotent)
        }

        // Verify payment
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new BookingException("Payment not successful");
        }

        // Verify lock ownership (seats should still be locked by this user)
        List<Long> seatIds = booking.getBookingSeats().stream()
                .map(bs -> bs.getSeat().getId())
                .toList();

        if (!seatLockService.verifyLockOwnership(booking.getShow().getId(), seatIds, booking.getUser().getId())) {
            throw new BookingException("Seat locks expired or invalid");
        }

        // Confirm booking
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        // Update show availability
        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() - booking.getNumberOfSeats());
        showRepository.save(show);

        // Release Redis locks (no longer needed, seats are booked in DB)
        seatLockService.unlockSeats(booking.getShow().getId(), seatIds);

        return booking;
    }

    /**
     * Cancel booking (user request or admin action)
     */
    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new BookingException("Booking already cancelled");
        }

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new BookingException("Only confirmed bookings can be cancelled");
        }

        // Mark as cancelled
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);

        // Restore show availability
        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() + booking.getNumberOfSeats());
        showRepository.save(show);

        // Initiate refund (in real system, call payment gateway)
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        return booking;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public Booking getBookingByCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Booking> getShowBookings(Long showId) {
        return bookingRepository.findByShowId(showId);
    }

    private BigDecimal getPriceForSeat(Show show, Seat seat) {
        return switch (seat.getSeatType()) {
            case PREMIUM -> show.getPremiumPrice();
            case VIP -> show.getVipPrice();
            default -> show.getRegularPrice();
        };
    }

    private String generateBookingCode() {
        return "BKG" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    /**
     * Get booking statistics for admin dashboard
     */
    public java.util.Map<String, Object> getBookingStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        // Total confirmed bookings
        long totalBookings = bookingRepository.countByStatus(Booking.BookingStatus.CONFIRMED);
        stats.put("totalBookings", totalBookings);

        // Total revenue from confirmed bookings
        java.math.BigDecimal totalRevenue = bookingRepository.sumTotalAmountByStatus(Booking.BookingStatus.CONFIRMED);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : java.math.BigDecimal.ZERO);

        // Today's bookings
        java.time.LocalDateTime startOfDay = java.time.LocalDate.now().atStartOfDay();
        java.time.LocalDateTime endOfDay = startOfDay.plusDays(1);
        long todayBookings = bookingRepository.countByStatusAndCreatedAtBetween(
                Booking.BookingStatus.CONFIRMED, startOfDay, endOfDay);
        stats.put("todayBookings", todayBookings);

        // Today's revenue
        java.math.BigDecimal todayRevenue = bookingRepository.sumTotalAmountByStatusAndCreatedAtBetween(
                Booking.BookingStatus.CONFIRMED, startOfDay, endOfDay);
        stats.put("todayRevenue", todayRevenue != null ? todayRevenue : java.math.BigDecimal.ZERO);

        return stats;
    }
}