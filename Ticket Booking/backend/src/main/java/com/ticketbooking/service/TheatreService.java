package com.ticketbooking.service;

import com.ticketbooking.entity.Screen;
import com.ticketbooking.entity.Seat;
import com.ticketbooking.entity.Theatre;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.repository.ScreenRepository;
import com.ticketbooking.repository.SeatRepository;
import com.ticketbooking.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    public List<Theatre> getAllTheatres() {
        return theatreRepository.findByActiveTrue();
    }

    public List<Theatre> getTheatresByCity(String city) {
        return theatreRepository.findByCityAndActiveTrue(city);
    }

    public Theatre getTheatreById(Long id) {
        return theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
    }

    public Theatre createTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    public Theatre updateTheatre(Long id, Theatre theatreDetails) {
        Theatre theatre = getTheatreById(id);

        theatre.setName(theatreDetails.getName());
        theatre.setCity(theatreDetails.getCity());
        theatre.setAddress(theatreDetails.getAddress());
        theatre.setZipCode(theatreDetails.getZipCode());
        theatre.setPhoneNumber(theatreDetails.getPhoneNumber());

        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(Long id) {
        Theatre theatre = getTheatreById(id);
        theatre.setActive(false);
        theatreRepository.save(theatre);
    }

    // Screen Management
    public Screen createScreen(Screen screen) {
        return screenRepository.save(screen);
    }

    public List<Screen> getScreensByTheatre(Long theatreId) {
        return screenRepository.findByTheatreIdAndActiveTrue(theatreId);
    }

    public Screen getScreenById(Long id) {
        return screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));
    }

    // Seat Management
    @Transactional
    public List<Seat> createSeatsForScreen(Long screenId, List<Seat> seats) {
        Screen screen = getScreenById(screenId);

        for (Seat seat : seats) {
            seat.setScreen(screen);
        }

        return seatRepository.saveAll(seats);
    }

    public List<Seat> getSeatsByScreen(Long screenId) {
        return seatRepository.findByScreenIdAndActiveTrue(screenId);
    }

    @Transactional
    public void deleteSeatsForScreen(Long screenId) {
        seatRepository.deleteByScreenId(screenId);
    }
}