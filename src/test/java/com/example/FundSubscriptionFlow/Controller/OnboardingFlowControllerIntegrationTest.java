package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.Controller.OnboardingFlowController;
import com.example.FundSubscriptionFlow.RequestModel.OnboardingFlowDTO;
import com.example.FundSubscriptionFlow.Service.OnboardingFlowService;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(OnboardingFlowController.class)
@AutoConfigureMockMvc
public class OnboardingFlowControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OnboardingFlowService onboardingFlowService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOnboardingFlow() throws Exception {
        OnboardingFlowDTO onboardingFlowDTO = new OnboardingFlowDTO();


        OnboardingFlowDTO createdOnboardingFlow = new OnboardingFlowDTO();
        createdOnboardingFlow.setId(UUID.randomUUID());


        when(onboardingFlowService.createOnboardingFlow(any(OnboardingFlowDTO.class))).thenReturn(createdOnboardingFlow);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/onboarding-flows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(onboardingFlowDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    public void testEditOnboardingFlow() throws Exception {
        UUID onboardingFlowId = UUID.randomUUID();
        OnboardingFlowDTO onboardingFlowDTO = new OnboardingFlowDTO();


        OnboardingFlowDTO updatedOnboardingFlow = new OnboardingFlowDTO();
        updatedOnboardingFlow.setId(onboardingFlowId);


        when(onboardingFlowService.editOnboardingFlow(any(UUID.class), any(OnboardingFlowDTO.class))).thenReturn(updatedOnboardingFlow);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/onboarding-flows/{id}", onboardingFlowId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(onboardingFlowDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(onboardingFlowId.toString()));
    }

    @Test
    public void testGetAllOnboardingFlows() throws Exception {
        UUID onboardingFlowId1 = UUID.randomUUID();
        UUID onboardingFlowId2 = UUID.randomUUID();
        OnboardingFlowDTO onboardingFlowDTO1 = OnboardingFlowDTO.builder().id(UUID.randomUUID()).build();
        OnboardingFlowDTO onboardingFlowDTO2 = OnboardingFlowDTO.builder().id(UUID.randomUUID()).build();

        List<OnboardingFlowDTO> onboardingFlowList = Arrays.asList(onboardingFlowDTO1, onboardingFlowDTO2);

        when(onboardingFlowService.getAllOnboardingFlows()).thenReturn(onboardingFlowList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/onboarding-flows"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
