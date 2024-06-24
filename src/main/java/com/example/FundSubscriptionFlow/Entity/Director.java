package com.example.FundSubscriptionFlow.Entity;

import com.example.FundSubscriptionFlow.Entity.InstitutionalInvestorDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Embeddable
public class Director {

    private String firstName;
    private String lastName;
    private String countryOfResidence;



}