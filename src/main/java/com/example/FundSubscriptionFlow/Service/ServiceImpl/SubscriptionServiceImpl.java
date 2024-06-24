package com.example.FundSubscriptionFlow.Service.ServiceImpl;

import com.example.FundSubscriptionFlow.Entity.*;
import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorRepository;
import com.example.FundSubscriptionFlow.Repository.OnboardingFlowRepository;
import com.example.FundSubscriptionFlow.Repository.SubscriptionRepository;
import com.example.FundSubscriptionFlow.RequestModel.AnswerDTO;
import com.example.FundSubscriptionFlow.RequestModel.FundSubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionStateDTO;
import com.example.FundSubscriptionFlow.Service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of SubscriptionService for managing subscriptions to funds.
 */
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private OnboardingFlowRepository onboardingFlowRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * Subscribe an investor to a fund.
     *
     * @param status                  Whether the subscription is active or not.
     * @param investorId              The ID of the investor subscribing.
     * @param onBoardingFlowId        The ID of the onboarding flow.
     * @param intendedInvestmentAmount The intended investment amount.
     * @param answers                 List of answers provided during subscription.
     */
    @Override
    public void subscribeToFund(boolean status, UUID investorId, UUID onBoardingFlowId, BigDecimal intendedInvestmentAmount, List<AnswerDTO> answers) {
        try {
            Investor investor = investorRepository.findById(investorId)
                    .orElseThrow(() -> new EntityNotFoundException("Investor not found with id: " + investorId));

            OnboardingFlow onboardingFlow = onboardingFlowRepository.findById(onBoardingFlowId)
                    .orElseThrow(() -> new EntityNotFoundException("OnboardingFlow not found with id: " + onBoardingFlowId));

            if (intendedInvestmentAmount.compareTo(onboardingFlow.getFund().getMinimumInvestmentAmount()) < 0) {
                throw new IllegalArgumentException("Intended investment amount must be at least " + onboardingFlow.getFund().getMinimumInvestmentAmount());
            }
            validateAnswers(answers, onboardingFlow.getTasks());

            Subscription subscription = Subscription.builder()
                    .investor(investor)
                    .onboardingFlow(onboardingFlow)
                    .subscriptionDate(LocalDateTime.now())
                    .active(status)
                    .subscribedAmount(intendedInvestmentAmount)
                    .build();
            subscriptionRepository.save(subscription);
        } catch (Exception e) {
            String errorMessage = "Failed to subscribe investor " + investorId + " to fund " + onBoardingFlowId + ": " + e.getMessage();
            logger.error(errorMessage, e);
            throw new IllegalArgumentException(errorMessage, e);
        }
    }

    /**
     * Retrieve active subscriptions with total investment volume per fund.
     *
     * @return List of FundSubscriptionDTO representing active subscriptions.
     */
    @Override
    public List<FundSubscriptionDTO> getActiveSubscriptionsWithTotalInvestmentVolumePerFund() {
        try {
            Map<UUID, List<Subscription>> subscriptionsByFundId = getActiveSubscriptionsGroupedByFund();

            return subscriptionsByFundId.entrySet().stream()
                    .map(entry -> {
                        UUID fundId = entry.getKey();
                        List<Subscription> subscriptions = entry.getValue();

                        FundSubscriptionDTO fundSubscriptionDTO = FundSubscriptionDTO.builder()
                                .fundId(fundId)
                                .fundName(subscriptions.get(0).getOnboardingFlow().getFund().getName())
                                .build();

                        BigDecimal totalAmount = subscriptions.stream()
                                .map(Subscription::getSubscribedAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        fundSubscriptionDTO.setTotalAmount(totalAmount);

                        List<SubscriptionStateDTO> subscriptionDTOs = convertToSubscriptionStateDTO(subscriptions);
                        fundSubscriptionDTO.setActiveSubscriptions(subscriptionDTOs);

                        return fundSubscriptionDTO;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve active subscriptions with total investment volume per fund: " + e.getMessage();
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Retrieve all subscriptions with total investment volume per subscription.
     *
     * @return List of SubscriptionStateDTO representing all subscriptions.
     */
    @Override
    public List<SubscriptionStateDTO> getActiveSubscriptionsWithTotalInvestmentVolumePerSubscription() {
        try {
            List<Subscription> activeSubscriptions = subscriptionRepository.findActiveSubscriptions();
            return convertToSubscriptionStateDTO(activeSubscriptions);
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve all subscriptions with total investment volume: " + e.getMessage();
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Validate answers provided during subscription against task questions.
     *
     * @param answers List of answers provided.
     * @param tasks   List of tasks with questions to validate against.
     */
    private void validateAnswers(List<AnswerDTO> answers, List<Task> tasks) {
        if(tasks!= null)
        for (Task task : tasks) {
            for (Question question : task.getQuestions()) {
                AnswerDTO answerDTO = findAnswerByQuestionId(answers, question.getId());
                if (answerDTO == null && question.isMandatory()) {
                    throw new IllegalArgumentException("Answer for mandatory question '" + question.getText() + "' is missing.");
                }
            }
        }
    }

    /**
     * Find an answer in the list by question ID.
     *
     * @param answers   List of answers to search.
     * @param questionId ID of the question to find answer for.
     * @return AnswerDTO if found, null otherwise.
     */
    private AnswerDTO findAnswerByQuestionId(List<AnswerDTO> answers, UUID questionId) {
        return answers.stream()
                .filter(answerDTO -> answerDTO.getQuestionId().equals(questionId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Group active subscriptions by fund ID.
     *
     * @return Map containing subscriptions grouped by fund ID.
     */
    private Map<UUID, List<Subscription>> getActiveSubscriptionsGroupedByFund() {
        List<Subscription> activeSubscriptions = subscriptionRepository.findActiveSubscriptions();
        return activeSubscriptions.stream()
                .filter(subscription -> subscription.getOnboardingFlow() != null && subscription.getOnboardingFlow().getFund() != null)
                .collect(Collectors.groupingBy(subscription -> subscription.getOnboardingFlow().getFund().getId()));
    }


    /**
     * Convert a list of subscriptions to SubscriptionStateDTO objects.
     *
     * @param subscriptions List of subscriptions to convert.
     * @return List of SubscriptionStateDTO objects.
     */
    private List<SubscriptionStateDTO> convertToSubscriptionStateDTO(List<Subscription> subscriptions) {
        return subscriptions.stream()
                .map(subscription -> SubscriptionStateDTO.builder()
                        .subscriptionId(subscription.getId())
                        .intendedAmount(subscription.getSubscribedAmount())
                        .status(subscription.isActive())
                        .build())
                .collect(Collectors.toList());
    }
}
