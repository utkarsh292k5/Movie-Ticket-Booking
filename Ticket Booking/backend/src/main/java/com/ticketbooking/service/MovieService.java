package com.ticketbooking.service;

import com.ticketbooking.entity.Movie;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    public List<Movie> getAllMovies() {
        return movieRepository.findByActiveTrue();
    }
    
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }
    
    public List<Movie> getMoviesByStatus(Movie.MovieStatus status) {
        return movieRepository.findByStatusAndActiveTrue(status);
    }
    
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }
    
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        
        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setPosterUrl(movieDetails.getPosterUrl());
        movie.setTrailerUrl(movieDetails.getTrailerUrl());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setGenre(movieDetails.getGenre());
        movie.setDuration(movieDetails.getDuration());
        movie.setRating(movieDetails.getRating());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setStatus(movieDetails.getStatus());
        
        return movieRepository.save(movie);
    }
    
    public void deleteMovie(Long id) {
        Movie movie = getMovieById(id);
        movie.setActive(false);
        movieRepository.save(movie);
    }
}

