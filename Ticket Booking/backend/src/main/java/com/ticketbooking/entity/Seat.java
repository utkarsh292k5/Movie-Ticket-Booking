package com.ticketbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"screen_id", "row_label", "seat_number"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screen_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "theatre"})
    private Screen screen;

    @Column(nullable = false)
    private String rowLabel; // A, B, C, etc.

    @Column(nullable = false)
    private Integer seatNumber; // 1, 2, 3, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType = SeatType.REGULAR;

    @Column(nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum SeatType {
        REGULAR, PREMIUM, VIP
    }
}