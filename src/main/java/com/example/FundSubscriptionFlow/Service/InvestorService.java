package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.Entity.Investor;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing Investors.
 */
public interface InvestorService {

    /**
     * Creates a new Investor based on the provided type and details.
     *
     * @param type    The UUID of the InvestorType for the new Investor.
     * @param details The details of the Investor as JSON.
     * @return The newly created Investor.
     * @throws JsonProcessingException If there is an issue processing the JSON details.
     * @throws InvestorTypeException   If validation of InvestorType fails.
     */
    Investor createInvestor(UUID type, JsonNode details) throws JsonProcessingException, InvestorTypeException;

    /**
     * Retrieves an Investor by its UUID.
     *
     * @param id The UUID of the Investor to retrieve.
     * @return The Investor.
     */
    Investor getInvestor(UUID id);

    /**
     * Retrieves all Investors.
     *
     * @return List of all Investors.
     */
    List<Investor> getAllInvestors();
}
