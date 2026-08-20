package com.seatflow.seat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false, unique = true, length = 30)
    private String seatNumber;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "has_outlet", nullable = false)
    private boolean hasOutlet;

    @Column(name = "near_window", nullable = false)
    private boolean nearWindow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status;

    protected Seat() {
    }

    public Seat(String seatNumber, String location, boolean hasOutlet, boolean nearWindow, SeatStatus status) {
        this.seatNumber = seatNumber;
        this.location = location;
        this.hasOutlet = hasOutlet;
        this.nearWindow = nearWindow;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getLocation() {
        return location;
    }

    public boolean isHasOutlet() {
        return hasOutlet;
    }

    public boolean isNearWindow() {
        return nearWindow;
    }

    public SeatStatus getStatus() {
        return status;
    }
}
