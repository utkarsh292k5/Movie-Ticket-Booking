package com.ticketbooking.controller;

import com.ticketbooking.dto.SeatAvailabilityDTO;
import com.ticketbooking.entity.Show;
import com.ticketbooking.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ShowController {
    
    @Autowired
    private ShowService showService;
    
    @GetMapping("/shows")
    public ResponseEntity<List<Show>> getShows(
            @RequestParam Long movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String city) {
        
        if (city != null && !city.isEmpty()) {
            return ResponseEntity.ok(showService.getShowsByMovieAndCity(movieId, date, city));
        }
        return ResponseEntity.ok(showService.getShowsByMovie(movieId, date));
    }
    
    @GetMapping("/shows/{id}")
    public ResponseEntity<Show> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }
    
    /**
     * CRITICAL: Real-time seat availability
     * Combines DB bookings + Redis locks
     */
    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<SeatAvailabilityDTO>> getSeatAvailability(@PathVariable Long showId) {
        return ResponseEntity.ok(showService.getSeatAvailability(showId));
    }
    
    @PostMapping("/admin/shows")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> createShow(@RequestBody Show show) {
        return ResponseEntity.status(HttpStatus.CREATED).body(showService.createShow(show));
    }
    
    @PutMapping("/admin/shows/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> updateShow(@PathVariable Long id, @RequestBody Show show) {
        return ResponseEntity.ok(showService.updateShow(id, show));
    }
    
    @DeleteMapping("/admin/shows/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
}

