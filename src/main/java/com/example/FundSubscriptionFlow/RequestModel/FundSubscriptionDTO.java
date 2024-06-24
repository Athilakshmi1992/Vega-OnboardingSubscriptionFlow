package com.example.FundSubscriptionFlow.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundSubscriptionDTO {
    private UUID fundId;
    private String fundName;
    private BigDecimal totalAmount;
    private List<SubscriptionStateDTO> activeSubscriptions;


}


