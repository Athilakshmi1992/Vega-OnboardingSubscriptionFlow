package com.example.FundSubscriptionFlow.Repository;

import com.example.FundSubscriptionFlow.Entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, UUID> {
}
