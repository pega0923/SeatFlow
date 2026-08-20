package com.seatflow.seat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void returnsSeatsOrderedBySeatNumber() throws Exception {
        seatRepository.save(new Seat("B-02", "2nd floor", false, true, SeatStatus.UNAVAILABLE));
        seatRepository.save(new Seat("A-01", "1st floor", true, false, SeatStatus.AVAILABLE));

        mockMvc.perform(get("/api/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value("A-01"))
                .andExpect(jsonPath("$[0].location").value("1st floor"))
                .andExpect(jsonPath("$[0].hasOutlet").value(true))
                .andExpect(jsonPath("$[0].nearWindow").value(false))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[1].seatNumber").value("B-02"));
    }
}
