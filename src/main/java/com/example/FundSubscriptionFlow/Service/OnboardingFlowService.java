package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.RequestModel.OnboardingFlowDTO;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing OnboardingFlows.
 */
@Service
public interface OnboardingFlowService {

    /**
     * Creates a new OnboardingFlow.
     *
     * @param onboardingFlowDTO The DTO containing data to create the OnboardingFlow.
     * @return The created OnboardingFlowDTO.
     */
    @Operation(summary = "Create a new OnboardingFlow")
    OnboardingFlowDTO createOnboardingFlow(OnboardingFlowDTO onboardingFlowDTO);

    /**
     * Edits an existing OnboardingFlow.
     *
     * @param flowId         The ID of the OnboardingFlow to edit.
     * @param updatedFlowDTO The DTO containing updated data for the OnboardingFlow.
     * @return The updated OnboardingFlowDTO.
     */
    @Operation(summary = "Edit an existing OnboardingFlow")
    OnboardingFlowDTO editOnboardingFlow(UUID flowId, OnboardingFlowDTO updatedFlowDTO);

    /**
     * Retrieves all OnboardingFlows.
     *
     * @return A list of all OnboardingFlowDTOs.
     */
    @Operation(summary = "Get all OnboardingFlows")
    List<OnboardingFlowDTO> getAllOnboardingFlows();
}
