package com.example.FundSubscriptionFlow.RequestModel;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorDTO {

    private UUID id;
    private UUID type;
    private JsonNode details; // Use Object for flexibility in JSON deserialization


}
