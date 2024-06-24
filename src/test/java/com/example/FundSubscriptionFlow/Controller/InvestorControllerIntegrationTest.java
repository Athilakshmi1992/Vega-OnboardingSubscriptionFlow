package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.Entity.Investor;
import com.example.FundSubscriptionFlow.Entity.InvestorType;
import com.example.FundSubscriptionFlow.RequestModel.InvestorDTO;
import com.example.FundSubscriptionFlow.Service.InvestorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(InvestorController.class)
@AutoConfigureMockMvc
public class InvestorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestorService investorService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetInvestorById() throws Exception {
        UUID investorId = UUID.randomUUID();
        Investor investor = new Investor(investorId, InvestorType.builder().type("Individual").build(), "John Doe");

        when(investorService.getInvestor(investorId)).thenReturn(investor);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/investors/{investorId}", investorId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(investorId.toString()));

    }

    @Test
    public void testGetAllInvestors() throws Exception {
        Investor investor1 = new Investor(UUID.randomUUID(), InvestorType.builder().type("Individual").build(), "John Doe");
        Investor investor2 = new Investor(UUID.randomUUID(), InvestorType.builder().type("Corporate").build(), "Company XYZ");
        List<Investor> investorList = Arrays.asList(investor1, investor2);

        when(investorService.getAllInvestors()).thenReturn(investorList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/investors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(investor1.getId().toString()));

    }
}
