package com.example.FundSubscriptionFlow.RequestModel;

import com.example.FundSubscriptionFlow.Entity.OnboardingFlow;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Setter
public class OnboardingFlowDTO {

    private UUID id;
    private UUID fundId;
    private UUID investorTypeId;
    private BigDecimal minimumInvestment;
    private List<TaskDTO> tasks;

    public OnboardingFlowDTO(OnboardingFlow onboardingFlow) {
        this.id = onboardingFlow.getId();
        this.fundId = onboardingFlow.getFund().getId();
        this.investorTypeId = onboardingFlow.getInvestorType().getId();
        this.minimumInvestment = onboardingFlow.getMinimumInvestment();
        this.tasks = onboardingFlow.getTasks().stream().map(TaskDTO::new).collect(Collectors.toList());
    }


    public OnboardingFlowDTO(UUID onboardingFlowId1, String s) {
    }
}
