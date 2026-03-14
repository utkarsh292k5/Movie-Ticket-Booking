package com.ticketbooking.repository;

import com.ticketbooking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByBookingId(Long bookingId);
    
    @Query("SELECT bs.seat.id FROM BookingSeat bs WHERE bs.show.id = :showId " +
           "AND bs.booking.status = 'CONFIRMED'")
    List<Long> findBookedSeatIdsByShowId(@Param("showId") Long showId);
    
    boolean existsByShowIdAndSeatId(Long showId, Long seatId);
}

