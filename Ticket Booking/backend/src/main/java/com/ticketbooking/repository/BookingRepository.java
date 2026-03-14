package com.ticketbooking.repository;

import com.ticketbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Booking> findByBookingCode(String bookingCode);
    Optional<Booking> findByIdempotencyKey(String idempotencyKey);
    List<Booking> findByShowId(Long showId);

    // Stats queries
    long countByStatus(Booking.BookingStatus status);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = :status")
    BigDecimal sumTotalAmountByStatus(@Param("status") Booking.BookingStatus status);

    long countByStatusAndCreatedAtBetween(Booking.BookingStatus status, LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = :status AND b.createdAt BETWEEN :start AND :end")
    BigDecimal sumTotalAmountByStatusAndCreatedAtBetween(
            @Param("status") Booking.BookingStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}