package com.example.FundSubscriptionFlow.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundDTO {

    private UUID id;
    private String name;
    private BigDecimal minimumInvestmentAmount;


    public FundDTO(String testFund, BigDecimal bigDecimal) {
    }
}
