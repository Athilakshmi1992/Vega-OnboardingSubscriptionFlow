package com.example.FundSubscriptionFlow.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InvestorType {
    @Id
    private UUID id;

    private String type;

  /*  @OneToMany(mappedBy = "investorType")
    private List<Investor> investor;*/

}