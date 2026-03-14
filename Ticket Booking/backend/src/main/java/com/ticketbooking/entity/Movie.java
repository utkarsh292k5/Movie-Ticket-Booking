package com.ticketbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    private String posterUrl;
    
    private String trailerUrl;
    
    @Column(nullable = false)
    private String language;
    
    @Column(nullable = false)
    private String genre;
    
    private Integer duration; // in minutes
    
    private String rating; // PG, PG-13, R, etc.
    
    private LocalDate releaseDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieStatus status = MovieStatus.NOW_SHOWING;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    public enum MovieStatus {
        NOW_SHOWING, UPCOMING, ENDED
    }
}

