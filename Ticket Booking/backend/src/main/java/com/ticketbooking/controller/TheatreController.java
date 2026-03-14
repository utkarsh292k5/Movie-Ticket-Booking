package com.ticketbooking.controller;

import com.ticketbooking.entity.Screen;
import com.ticketbooking.entity.Seat;
import com.ticketbooking.entity.Theatre;
import com.ticketbooking.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping("/theatres")
    public ResponseEntity<List<Theatre>> getTheatres(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return ResponseEntity.ok(theatreService.getTheatresByCity(city));
        }
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }

    @GetMapping("/theatres/{id}")
    public ResponseEntity<Theatre> getTheatreById(@PathVariable Long id) {
        return ResponseEntity.ok(theatreService.getTheatreById(id));
    }

    @PostMapping("/admin/theatres")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theatreService.createTheatre(theatre));
    }

    @PutMapping("/admin/theatres/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Theatre> updateTheatre(@PathVariable Long id, @RequestBody Theatre theatre) {
        return ResponseEntity.ok(theatreService.updateTheatre(id, theatre));
    }

    @DeleteMapping("/admin/theatres/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return ResponseEntity.noContent().build();
    }

    // Screen endpoints
    @GetMapping("/theatres/{theatreId}/screens")
    public ResponseEntity<List<Screen>> getScreensByTheatre(@PathVariable Long theatreId) {
        return ResponseEntity.ok(theatreService.getScreensByTheatre(theatreId));
    }

    @PostMapping("/admin/screens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screen> createScreen(@RequestBody Screen screen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(theatreService.createScreen(screen));
    }

    // Seat endpoints
    @GetMapping("/screens/{screenId}/seats")
    public ResponseEntity<List<Seat>> getSeatsByScreen(@PathVariable Long screenId) {
        return ResponseEntity.ok(theatreService.getSeatsByScreen(screenId));
    }

    @PostMapping("/admin/screens/{screenId}/seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Seat>> createSeats(@PathVariable Long screenId, @RequestBody List<Seat> seats) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(theatreService.createSeatsForScreen(screenId, seats));
    }

    @DeleteMapping("/admin/screens/{screenId}/seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSeatsForScreen(@PathVariable Long screenId) {
        theatreService.deleteSeatsForScreen(screenId);
        return ResponseEntity.noContent().build();
    }
}