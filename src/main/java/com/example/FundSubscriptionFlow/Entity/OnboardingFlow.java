package com.example.FundSubscriptionFlow.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "onboarding_flow", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fund_id", "investor_type_id"})
})
public class OnboardingFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;

    @ManyToOne
    @JoinColumn(name = "investor_type_id")
    private InvestorType investorType;
    private BigDecimal minimumInvestment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "onboarding_flow_task",
            joinColumns = @JoinColumn(name = "onboarding_flow_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;
}
