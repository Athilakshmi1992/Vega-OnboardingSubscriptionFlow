package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.Entity.*;
import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorRepository;
import com.example.FundSubscriptionFlow.Repository.OnboardingFlowRepository;
import com.example.FundSubscriptionFlow.Repository.SubscriptionRepository;
import com.example.FundSubscriptionFlow.RequestModel.AnswerDTO;
import com.example.FundSubscriptionFlow.RequestModel.FundSubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionStateDTO;
import com.example.FundSubscriptionFlow.Service.ServiceImpl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private OnboardingFlowRepository onboardingFlowRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private UUID investorId;
    private UUID onboardingFlowId;
    private BigDecimal intendedInvestmentAmount;
    private List<AnswerDTO> answers;

    @BeforeEach
    void setUp() {
        investorId = UUID.randomUUID();
        onboardingFlowId = UUID.randomUUID();
        intendedInvestmentAmount = BigDecimal.valueOf(5000);
        answers = new ArrayList<>();
    }

    @Test
    void testSubscribeToFund_Successful() {
        // Mock repositories to return valid entities
        Investor investor = new Investor();
        investor.setId(investorId);
        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));

        Fund fund = new Fund();
        fund.setId(UUID.randomUUID());
        fund.setMinimumInvestmentAmount(BigDecimal.valueOf(1000));
        OnboardingFlow onboardingFlow = new OnboardingFlow();
        onboardingFlow.setId(onboardingFlowId);
        onboardingFlow.setFund(fund);
        when(onboardingFlowRepository.findById(onboardingFlowId)).thenReturn(Optional.of(onboardingFlow));

        // Mock subscriptionRepository save method
        doAnswer(invocation -> {
            Subscription savedSubscription = invocation.getArgument(0);
            assertNotNull(savedSubscription);
            assertEquals(investorId, savedSubscription.getInvestor().getId());
            assertEquals(onboardingFlowId, savedSubscription.getOnboardingFlow().getId());
            assertEquals(intendedInvestmentAmount, savedSubscription.getSubscribedAmount());
            assertTrue(savedSubscription.isActive());
            assertNotNull(savedSubscription.getSubscriptionDate());
            return null;
        }).when(subscriptionRepository).save(any(Subscription.class));

        // Test the service method
        subscriptionService.subscribeToFund(true, investorId, onboardingFlowId, intendedInvestmentAmount, answers);

        // Verify that subscriptionRepository.save was called once
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void testSubscribeToFund_InvalidInvestmentAmount() {
        // Mock repositories to return valid entities
        Investor investor = new Investor();
        investor.setId(investorId);
        when(investorRepository.findById(investorId)).thenReturn(Optional.of(investor));

        Fund fund = new Fund();
        fund.setId(UUID.randomUUID());
        fund.setMinimumInvestmentAmount(BigDecimal.valueOf(10000)); // higher minimum investment amount
        OnboardingFlow onboardingFlow = new OnboardingFlow();
        onboardingFlow.setId(onboardingFlowId);
        onboardingFlow.setFund(fund);
        when(onboardingFlowRepository.findById(onboardingFlowId)).thenReturn(Optional.of(onboardingFlow));

        // Test with intended investment amount less than minimum
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            subscriptionService.subscribeToFund(true, investorId, onboardingFlowId, intendedInvestmentAmount, answers);
        });
        assertTrue(exception.getMessage().contains("Intended investment amount must be at least"));

        // Verify that subscriptionRepository.save was not called
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void testGetActiveSubscriptionsWithTotalInvestmentVolumePerFund() {
        // Mock repository to return active subscriptions
        Subscription subscription1 = createSubscription(UUID.randomUUID(), true, BigDecimal.valueOf(5000));
        Subscription subscription2 = createSubscription(UUID.randomUUID(), true, BigDecimal.valueOf(3000));
        List<Subscription> activeSubscriptions = Arrays.asList(subscription1, subscription2);
        when(subscriptionRepository.findActiveSubscriptions()).thenReturn(activeSubscriptions);

        // Test the service method
        List<FundSubscriptionDTO> fundSubscriptionDTOs = subscriptionService.getActiveSubscriptionsWithTotalInvestmentVolumePerFund();

        // Assertions
        assertNotNull(fundSubscriptionDTOs);


        // Verify the content of fundSubscriptionDTOs
        for (FundSubscriptionDTO dto : fundSubscriptionDTOs) {
            assertTrue(dto.getFundId().equals(subscription1.getOnboardingFlow().getFund().getId())
                    || dto.getFundId().equals(subscription2.getOnboardingFlow().getFund().getId()));
            BigDecimal totalAmount = dto.getActiveSubscriptions().stream()
                    .map(SubscriptionStateDTO::getIntendedAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            assertEquals(subscription1.getSubscribedAmount().add(subscription2.getSubscribedAmount()), totalAmount);
        }
    }

    @Test
    void testGetActiveSubscriptionsWithTotalInvestmentVolumePerSubscription() {
        // Mock repository to return active subscriptions
        Subscription subscription1 = createSubscription(UUID.randomUUID(), true, BigDecimal.valueOf(5000));
        Subscription subscription2 = createSubscription(UUID.randomUUID(), true, BigDecimal.valueOf(3000));
        List<Subscription> activeSubscriptions = Arrays.asList(subscription1, subscription2);
        when(subscriptionRepository.findActiveSubscriptions()).thenReturn(activeSubscriptions);

        // Test the service method
        List<SubscriptionStateDTO> subscriptionDTOs = subscriptionService.getActiveSubscriptionsWithTotalInvestmentVolumePerSubscription();

        // Assertions
        assertNotNull(subscriptionDTOs);
        assertEquals(2, subscriptionDTOs.size());

        // Verify the content of subscriptionDTOs
        for (SubscriptionStateDTO dto : subscriptionDTOs) {
            assertTrue(dto.getSubscriptionId().equals(subscription1.getId()) || dto.getSubscriptionId().equals(subscription2.getId()));
        }
    }

    // Helper method to create Subscription instance
    private Subscription createSubscription(UUID id, boolean active, BigDecimal subscribedAmount) {
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setActive(active);
        subscription.setSubscribedAmount(subscribedAmount);
        subscription.setOnboardingFlow(new OnboardingFlow());
        return subscription;
    }
}
