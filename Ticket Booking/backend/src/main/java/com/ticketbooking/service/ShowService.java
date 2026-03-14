package com.ticketbooking.service;

import com.ticketbooking.dto.SeatAvailabilityDTO;
import com.ticketbooking.entity.Seat;
import com.ticketbooking.entity.Show;
import com.ticketbooking.exception.ResourceNotFoundException;
import com.ticketbooking.repository.BookingSeatRepository;
import com.ticketbooking.repository.SeatRepository;
import com.ticketbooking.repository.ShowRepository;
import com.ticketbooking.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingSeatRepository bookingSeatRepository;

    @Autowired
    private SeatLockService seatLockService;

    @Autowired
    private ScreenRepository screenRepository;

    public Show createShow(Show show) {
        // Get the screen to set available seats
        if (show.getScreen() != null && show.getScreen().getId() != null) {
            var screen = screenRepository.findById(show.getScreen().getId()).orElse(null);
            if (screen != null) {
                show.setAvailableSeats(screen.getTotalSeats());
                show.setScreen(screen);
            }
        }
        return showRepository.save(show);
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
    }

    public List<Show> getShowsByMovie(Long movieId, LocalDate date) {
        return showRepository.findByMovieIdAndShowDateAndActiveTrue(movieId, date);
    }

    public List<Show> getShowsByMovieAndCity(Long movieId, LocalDate date, String city) {
        return showRepository.findByMovieAndDateAndCity(movieId, date, city);
    }

    public Show updateShow(Long id, Show showDetails) {
        Show show = getShowById(id);

        show.setShowDate(showDetails.getShowDate());
        show.setShowTime(showDetails.getShowTime());
        show.setRegularPrice(showDetails.getRegularPrice());
        show.setPremiumPrice(showDetails.getPremiumPrice());
        show.setVipPrice(showDetails.getVipPrice());
        show.setStatus(showDetails.getStatus());

        return showRepository.save(show);
    }

    public void deleteShow(Long id) {
        Show show = getShowById(id);
        show.setActive(false);
        showRepository.save(show);
    }

    /**
     * CRITICAL: Get real-time seat availability for a show
     * Combines database bookings + Redis locks
     */
    public List<SeatAvailabilityDTO> getSeatAvailability(Long showId) {
        Show show = getShowById(showId);
        List<Seat> allSeats = seatRepository.findByScreenId(show.getScreen().getId());

        // Get booked seat IDs from database
        List<Long> bookedSeatIds = bookingSeatRepository.findBookedSeatIdsByShowId(showId);

        // Get locked seat IDs from Redis
        List<Long> allSeatIds = allSeats.stream().map(Seat::getId).toList();
        List<Long> lockedSeatIds = seatLockService.getLockedSeats(showId, allSeatIds);

        List<SeatAvailabilityDTO> availability = new ArrayList<>();

        for (Seat seat : allSeats) {
            String status;
            if (bookedSeatIds.contains(seat.getId())) {
                status = "BOOKED";
            } else if (lockedSeatIds.contains(seat.getId())) {
                status = "LOCKED";
            } else {
                status = "AVAILABLE";
            }

            BigDecimal price = getPriceForSeatType(show, seat.getSeatType());

            availability.add(new SeatAvailabilityDTO(
                    seat.getId(),
                    seat.getRowLabel(),
                    seat.getSeatNumber(),
                    seat.getSeatType().name(),
                    status,
                    price.toString()
            ));
        }

        return availability;
    }

    private BigDecimal getPriceForSeatType(Show show, Seat.SeatType seatType) {
        return switch (seatType) {
            case PREMIUM -> show.getPremiumPrice();
            case VIP -> show.getVipPrice();
            default -> show.getRegularPrice();
        };
    }
}