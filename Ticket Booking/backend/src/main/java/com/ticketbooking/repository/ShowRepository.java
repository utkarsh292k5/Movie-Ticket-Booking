package com.ticketbooking.repository;

import com.ticketbooking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieIdAndShowDateAndActiveTrue(Long movieId, LocalDate showDate);

    List<Show> findByScreenIdAndShowDateAndActiveTrue(Long screenId, LocalDate showDate);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.showDate = :showDate " +
            "AND s.screen.theatre.city = :city AND s.active = true")
    List<Show> findByMovieAndDateAndCity(@Param("movieId") Long movieId,
                                         @Param("showDate") LocalDate showDate,
                                         @Param("city") String city);

    List<Show> findByMovieIdAndActiveTrue(Long movieId);
}