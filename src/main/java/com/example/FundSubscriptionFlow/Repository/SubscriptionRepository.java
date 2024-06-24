package com.example.FundSubscriptionFlow.Repository;

import com.example.FundSubscriptionFlow.Entity.Subscription;
import com.example.FundSubscriptionFlow.RequestModel.FundSubscriptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    @Query("SELECT s FROM Subscription s WHERE s.active = true")
    List<Subscription> findActiveSubscriptions();


}
