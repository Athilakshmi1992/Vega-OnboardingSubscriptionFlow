package com.example.FundSubscriptionFlow.Repository;

import com.example.FundSubscriptionFlow.Entity.InvestorType;
import com.example.FundSubscriptionFlow.Entity.OnboardingFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingFlowRepository extends JpaRepository<OnboardingFlow, UUID> {

      Optional<OnboardingFlow> findByFundIdAndInvestorType(UUID fundId, InvestorType investmentType);
}
