package com.example.FundSubscriptionFlow.Repository;

import com.example.FundSubscriptionFlow.Entity.InvestorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvestorTypeRepository extends JpaRepository<InvestorType, UUID> {
}