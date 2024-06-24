package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.Entity.Answer;
import com.example.FundSubscriptionFlow.RequestModel.AnswerDTO;
import com.example.FundSubscriptionFlow.RequestModel.FundSubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionStateDTO;
import com.example.FundSubscriptionFlow.Service.SubscriptionService;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc
public class SubscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubscribeToFund() throws Exception {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(UUID.randomUUID(), UUID.randomUUID(), List.of(AnswerDTO.builder().answerText("answer").build()), new BigDecimal(1000.0), true);

        // when(subscriptionService.subscribeToFund(any(Boolean.class), any(UUID.class), any(UUID.class), any(BigDecimal.class), any())); // Assuming subscribeToFund returns a success indicator

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subscriptions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Successfully subscribed to fund."));
    }

    @Test
    public void testGetActiveSubscriptionsWithTotalInvestmentVolumePerFund() throws Exception {
        UUID fundId1 = UUID.randomUUID();
        UUID fundId2 = UUID.randomUUID();
        SubscriptionStateDTO subscription2 = new SubscriptionStateDTO(UUID.randomUUID(), new BigDecimal(3000.0), true);
        FundSubscriptionDTO fundSubscriptionDTO = new FundSubscriptionDTO(UUID.randomUUID(), "fund1", new BigDecimal(5000.0), List.of(subscription2));


        List<FundSubscriptionDTO> subscriptions = Arrays.asList(fundSubscriptionDTO);

        when(subscriptionService.getActiveSubscriptionsWithTotalInvestmentVolumePerFund()).thenReturn(subscriptions);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/subscriptions/funds"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllSubscriptionsWithTotalInvestmentVolume() throws Exception {
        SubscriptionStateDTO subscription1 = new SubscriptionStateDTO(UUID.randomUUID(), new BigDecimal(5000.0), true);
        SubscriptionStateDTO subscription2 = new SubscriptionStateDTO(UUID.randomUUID(), new BigDecimal(3000.0), true);

        List<SubscriptionStateDTO> subscriptions = Arrays.asList(subscription1, subscription2);

        when(subscriptionService.getActiveSubscriptionsWithTotalInvestmentVolumePerSubscription()).thenReturn(subscriptions);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/subscriptions/subscriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].subscriptionId").exists());
                   }
}
