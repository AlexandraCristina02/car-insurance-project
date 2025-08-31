package com.example.carins;

import com.example.carins.service.CarService;
import com.example.carins.service.ScheduledTaskPolicyDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScheduledTaskPolicyDate task;

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
        mockMvc.perform(post("/api/cars/1/claims").content("""
                {
                    "claimDate": "2026-07-30",
                    "description": "Rear-end accident",
                    "amount": 1200.00
                }
                """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void getCarHistory_404NotFound() throws Exception{
      mockMvc.perform(get("/api/cars/185678/history")).andExpect(status().isNotFound());
    }

    @Test
    void getCarHistory_200OK() throws Exception{
        mockMvc.perform(get("/api/cars/1/history")).andExpect(status().isOk());
    }

    @Test
    void isInsuranceValid_carIdNotFound() throws Exception{
        mockMvc.perform(get("/api/cars/56/insurance-valid?date=2025-03-23")).andExpect(status().isNotFound());
    }

    @Test
    void isInsuranceValid_invalidDateFormat() throws Exception{
        mockMvc.perform(get("/api/cars/1/insurance-valid?date=20-03-2025")).andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid date format. Expected format: YYYY-MM-DD"));
    }

    @Test
    void isInsuranceValid_dateOutsideRange() throws Exception{
        mockMvc.perform(get("/api/cars/1/insurance-valid?date=1800-03-23")).andExpect(status().isBadRequest())
                .andExpect(content().string("Date outside supported range 1900-01-01 to 2100-12-31"));
    }

    @Test
    void createPolicy_nullEndDate() throws Exception{
        mockMvc.perform(post("/api/policy").content("""
                {
                    "carId": "1",
                    "provider": "Allianz",
                    "startDate": "2025-01-01",
                    "endDate: ""
                }
                """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    void createPolicy_withoutEndDate() throws Exception{
        mockMvc.perform(post("/api/policy").content("""
                {
                    "carId": "1",
                    "provider": "Allianz",
                    "startDate": "2025-01-01"
                }
                """).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

    @Test
    public void testLogExpiredPolicies() {
        task.logExpiredPolicies();
    }
}
