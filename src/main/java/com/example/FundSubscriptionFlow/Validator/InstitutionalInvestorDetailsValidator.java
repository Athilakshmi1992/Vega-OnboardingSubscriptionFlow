package com.example.FundSubscriptionFlow.Validator;

import com.example.FundSubscriptionFlow.Entity.InstitutionalInvestorDetails;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator implementation for validating InstitutionalInvestorDetails.
 * Implements InvestorDetailsValidator interface.
 */
@Component
public class InstitutionalInvestorDetailsValidator implements InvestorDetailsValidator {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Validates the provided details object as InstitutionalInvestorDetails.
     *
     * @param details The details object to validate.
     * @throws InvestorTypeException if the details object is invalid for InstitutionalInvestorDetails.
     */
    @Override
    public void validate(Object details) throws InvestorTypeException {
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            mapper.convertValue(details, InstitutionalInvestorDetails.class);
        } catch (Exception e) {
            throw new InvestorTypeException("Invalid details for Institutional investor");
        }
    }
}
