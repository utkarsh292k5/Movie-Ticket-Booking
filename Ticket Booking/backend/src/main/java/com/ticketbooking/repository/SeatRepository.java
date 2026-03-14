package com.ticketbooking.repository;

import com.ticketbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreenIdAndActiveTrue(Long screenId);
    List<Seat> findByScreenId(Long screenId);
    void deleteByScreenId(Long screenId);
}