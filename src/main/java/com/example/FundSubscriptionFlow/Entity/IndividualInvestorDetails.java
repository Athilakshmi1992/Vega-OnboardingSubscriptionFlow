package com.example.FundSubscriptionFlow.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class    IndividualInvestorDetails {

    private String firstName;
    private String lastName;
    private String countryOfResidence;

}