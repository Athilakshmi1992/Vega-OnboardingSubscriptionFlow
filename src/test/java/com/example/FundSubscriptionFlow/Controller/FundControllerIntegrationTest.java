package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.Controller.FundController;
import com.example.FundSubscriptionFlow.RequestModel.FundDTO;
import com.example.FundSubscriptionFlow.Service.FundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(FundController.class)
@AutoConfigureMockMvc
public class FundControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundService fundService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFund() throws Exception {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setName("Test Fund");
        fundDTO.setMinimumInvestmentAmount(BigDecimal.valueOf(1000));

        FundDTO createdFund = new FundDTO(UUID.randomUUID(), "Test Fund", BigDecimal.valueOf(1000));

        when(fundService.createFund(any(FundDTO.class))).thenReturn(createdFund);

        mockMvc.perform(MockMvcRequestBuilders.post("/funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdFund.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Fund"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minimumInvestmentAmount").value(1000));
    }

    @Test
    public void testUpdateFund() throws Exception {
        UUID fundId = UUID.randomUUID();
        FundDTO fundDTO = new FundDTO(fundId, "Updated Fund", BigDecimal.valueOf(1500));

        FundDTO updatedFund = new FundDTO(fundId, "Updated Fund", BigDecimal.valueOf(1500));

        when(fundService.updateFund(any(UUID.class), any(FundDTO.class))).thenReturn(updatedFund);

        mockMvc.perform(MockMvcRequestBuilders.put("/funds/{fundId}", fundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedFund.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Fund"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minimumInvestmentAmount").value(1500));
    }

    @Test
    public void testGetFundById() throws Exception {
        UUID fundId = UUID.randomUUID();
        FundDTO fundDTO = new FundDTO(fundId, "Test Fund", BigDecimal.valueOf(1000));

        when(fundService.getFundById(fundId)).thenReturn(fundDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/funds/{fundId}", fundId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(fundId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Fund"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minimumInvestmentAmount").value(1000));
    }

    @Test
    public void testGetAllFunds() throws Exception {
        FundDTO fund1 = new FundDTO(UUID.randomUUID(), "Fund 1", BigDecimal.valueOf(2000));
        FundDTO fund2 = new FundDTO(UUID.randomUUID(), "Fund 2", BigDecimal.valueOf(3000));
        List<FundDTO> fundDTOList = Arrays.asList(fund1, fund2);

        when(fundService.getAllFunds()).thenReturn(fundDTOList);

        mockMvc.perform(MockMvcRequestBuilders.get("/funds"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(fund1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Fund 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minimumInvestmentAmount").value(2000))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(fund2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Fund 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].minimumInvestmentAmount").value(3000));
    }
}
