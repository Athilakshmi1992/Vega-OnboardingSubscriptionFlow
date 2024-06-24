package com.example.FundSubscriptionFlow.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SubscriptionDTO {
    private UUID investorId;
    private UUID onBoardingFlowId;
    private List<AnswerDTO> answers;

    private BigDecimal subscribedAmount;

    private boolean active;

    public SubscriptionDTO(UUID investorId, UUID onBoardingFlowId, List<AnswerDTO> answers) {
        this.investorId = investorId;
        this.onBoardingFlowId = onBoardingFlowId;
        this.answers = answers;
    }
}
