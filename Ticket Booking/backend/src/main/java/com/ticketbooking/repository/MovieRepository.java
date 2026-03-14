package com.ticketbooking.repository;

import com.ticketbooking.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByActiveTrue();
    List<Movie> findByStatusAndActiveTrue(Movie.MovieStatus status);
    List<Movie> findByLanguageAndActiveTrue(String language);
    List<Movie> findByGenreAndActiveTrue(String genre);
}

