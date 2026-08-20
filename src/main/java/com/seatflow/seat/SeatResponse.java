package com.seatflow.seat;

public record SeatResponse(
        Long id,
        String seatNumber,
        String location,
        boolean hasOutlet,
        boolean nearWindow,
        SeatStatus status
) {

    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getLocation(),
                seat.isHasOutlet(),
                seat.isNearWindow(),
                seat.getStatus()
        );
    }
}
