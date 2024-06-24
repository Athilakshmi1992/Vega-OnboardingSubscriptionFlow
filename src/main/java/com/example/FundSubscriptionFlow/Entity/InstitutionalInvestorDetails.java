package com.example.FundSubscriptionFlow.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitutionalInvestorDetails {

    private String companyName;
    private String countryOfIncorporation;

    @OneToMany(mappedBy = "institutionalInvestor")
    private List<Director> directors;

}