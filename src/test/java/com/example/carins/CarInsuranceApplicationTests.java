package com.example.carins;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }

    @Test
    void createInsuranceClaim_success() throws Exception {
        mockMvc.perform(post("/api/cars/1/claims").content("""
                        {
                            "claimDate": "2025-07-30",
                            "description": "Rear-end accident",
                            "amount": 1200.00
                        }
                        """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.claimDate").value("2025-07-30"))
                .andExpect(jsonPath("$.description").value("Rear-end accident"))
                .andExpect(jsonPath("$.amount").value(1200.00));
    }

    @Test
    void createInsuranceClaim_fail() throws Exception {
        this.mockMvc.perform(post("/api/cars/1/claims").content("""
                {
                    "claimDate": "2026-07-30",
                    "description": "Rear-end accident",
                    "amount": 1200.00
                }
                """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
