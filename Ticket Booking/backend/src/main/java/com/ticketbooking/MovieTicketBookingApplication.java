package com.ticketbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MovieTicketBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieTicketBookingApplication.class, args);
    }
}

