package com.seatflow.seat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> getSeats() {
        return seatRepository.findAllByOrderBySeatNumberAsc().stream()
                .map(SeatResponse::from)
                .toList();
    }
}
