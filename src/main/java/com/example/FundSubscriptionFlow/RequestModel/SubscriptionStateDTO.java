package com.example.FundSubscriptionFlow.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionStateDTO {
    private UUID subscriptionId;
    private BigDecimal intendedAmount;
    private boolean status;

}