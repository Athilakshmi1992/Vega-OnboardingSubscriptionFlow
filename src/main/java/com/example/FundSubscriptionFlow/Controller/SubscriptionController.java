package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.RequestModel.FundSubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionDTO;
import com.example.FundSubscriptionFlow.RequestModel.SubscriptionStateDTO;
import com.example.FundSubscriptionFlow.Service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriptionService subscriptionService;

    @Operation(summary = "Subscribe to a fund")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully subscribed to fund"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToFund(@RequestBody SubscriptionDTO subscriptionDTO) {
        try {
            subscriptionService.subscribeToFund(subscriptionDTO.isActive(), subscriptionDTO.getInvestorId(),
                    subscriptionDTO.getOnBoardingFlowId(), subscriptionDTO.getSubscribedAmount(), subscriptionDTO.getAnswers());
            return ResponseEntity.ok("Successfully subscribed to fund.");
        } catch (Exception e) {
            String errorMessage = "Failed to subscribe to fund: " + e.getMessage();
            logger.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @Operation(summary = "Get active subscriptions with total investment volume per fund")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved active subscriptions per fund"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/funds")
    public List<FundSubscriptionDTO> getActiveSubscriptionsWithTotalInvestmentVolumePerFund() {
        try {
            return subscriptionService.getActiveSubscriptionsWithTotalInvestmentVolumePerFund();
        } catch (Exception e) {
            logger.error("Failed to retrieve active subscriptions with total investment volume per fund: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    @Operation(summary = "Get all subscriptions with total investment volume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all subscriptions with total investment volume"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/subscriptions")
    public List<SubscriptionStateDTO> getAllSubscriptionsWithTotalInvestmentVolume() {
        try {
            return subscriptionService.getActiveSubscriptionsWithTotalInvestmentVolumePerSubscription();
        } catch (Exception e) {
            logger.error("Failed to retrieve all subscriptions with total investment volume: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }
}
