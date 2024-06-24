package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.RequestModel.AnswerDTO;
import com.example.FundSubscriptionFlow.RequestModel.FundSubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionStateDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing subscriptions to funds.
 */
public interface SubscriptionService {

    /**
     * Subscribe an investor to a fund.
     *
     * @param status                  Whether the subscription is active or not.
     * @param investorId              The ID of the investor subscribing.
     * @param fundId                  The ID of the fund to subscribe to.
     * @param intendedInvestmentAmount The intended investment amount.
     * @param answers                 List of answers provided during subscription.
     */
    void subscribeToFund(boolean status, UUID investorId, UUID fundId, BigDecimal intendedInvestmentAmount, List<AnswerDTO> answers);

    /**
     * Retrieve active subscriptions with total investment volume per fund.
     *
     * @return List of FundSubscriptionDTO representing active subscriptions.
     */
    List<FundSubscriptionDTO> getActiveSubscriptionsWithTotalInvestmentVolumePerFund();

    /**
     * Retrieve all subscriptions with total investment volume per subscription.
     *
     * @return List of SubscriptionStateDTO representing all subscriptions.
     */
    List<SubscriptionStateDTO> getActiveSubscriptionsWithTotalInvestmentVolumePerSubscription();
}
