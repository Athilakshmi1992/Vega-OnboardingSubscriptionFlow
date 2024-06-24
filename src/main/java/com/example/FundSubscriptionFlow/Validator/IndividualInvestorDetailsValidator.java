package com.example.FundSubscriptionFlow.Validator;

import com.example.FundSubscriptionFlow.Entity.IndividualInvestorDetails;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator implementation for validating IndividualInvestorDetails.
 * Implements InvestorDetailsValidator interface.
 */
@Component
public class IndividualInvestorDetailsValidator implements InvestorDetailsValidator {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Validates the provided details object as IndividualInvestorDetails.
     *
     * @param details The details object to validate.
     * @throws InvestorTypeException if the details object is invalid for IndividualInvestorDetails.
     */
    @Override
    public void validate(Object details) throws InvestorTypeException {
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        try {
            mapper.convertValue(details, IndividualInvestorDetails.class);
        } catch (Exception e) {
            throw new InvestorTypeException("Invalid details for Individual investor");
        }
    }
}
