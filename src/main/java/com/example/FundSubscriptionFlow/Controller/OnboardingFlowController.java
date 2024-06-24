package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.RequestModel.OnboardingFlowDTO;
import com.example.FundSubscriptionFlow.Service.OnboardingFlowService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing OnboardingFlows.
 */
@RestController
@RequestMapping("/api/onboarding-flows")
public class OnboardingFlowController {

    @Autowired
    private OnboardingFlowService onboardingFlowService;

    /**
     * Creates a new OnboardingFlow.
     *
     * @param onboardingFlowDTO The DTO containing data to create the OnboardingFlow.
     * @return ResponseEntity with the created OnboardingFlowDTO.
     */
    @Operation(summary = "Create a new OnboardingFlow")
    @PostMapping
    public ResponseEntity<OnboardingFlowDTO> createOnboardingFlow(@RequestBody OnboardingFlowDTO onboardingFlowDTO) {

        OnboardingFlowDTO createdOnboardingFlow = onboardingFlowService.createOnboardingFlow(onboardingFlowDTO);
        return ResponseEntity.ok(createdOnboardingFlow);

    }

    /**
     * Edits an existing OnboardingFlow.
     *
     * @param id                The ID of the OnboardingFlow to edit.
     * @param onboardingFlowDTO The DTO containing updated data for the OnboardingFlow.
     * @return ResponseEntity with the updated OnboardingFlowDTO.
     */
    @Operation(summary = "Edit an existing OnboardingFlow")
    @PutMapping("/{id}")
    public ResponseEntity<OnboardingFlowDTO> editOnboardingFlow(@PathVariable UUID id, @RequestBody OnboardingFlowDTO onboardingFlowDTO) {

        OnboardingFlowDTO updatedOnboardingFlow = onboardingFlowService.editOnboardingFlow(id, onboardingFlowDTO);
        return ResponseEntity.ok(updatedOnboardingFlow);

    }

    /**
     * Retrieves all OnboardingFlows.
     *
     * @return ResponseEntity with a list of all OnboardingFlowDTOs.
     */
    @Operation(summary = "Get all OnboardingFlows")
    @GetMapping
    public ResponseEntity<List<OnboardingFlowDTO>> getAllOnboardingFlows() {
        List<OnboardingFlowDTO> onboardingFlows = onboardingFlowService.getAllOnboardingFlows();
        return ResponseEntity.ok(onboardingFlows);
    }
}
