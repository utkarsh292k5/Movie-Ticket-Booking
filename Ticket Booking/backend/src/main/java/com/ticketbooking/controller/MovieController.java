package com.ticketbooking.controller;

import com.ticketbooking.entity.Movie;
import com.ticketbooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    
    @GetMapping("/movies/status/{status}")
    public ResponseEntity<List<Movie>> getMoviesByStatus(@PathVariable String status) {
        Movie.MovieStatus movieStatus = Movie.MovieStatus.valueOf(status.toUpperCase());
        return ResponseEntity.ok(movieService.getMoviesByStatus(movieStatus));
    }
    
    @PostMapping("/admin/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(movie));
    }
    
    @PutMapping("/admin/movies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }
    
    @DeleteMapping("/admin/movies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}

