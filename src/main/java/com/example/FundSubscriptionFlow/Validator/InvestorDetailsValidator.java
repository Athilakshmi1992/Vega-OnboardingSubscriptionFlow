package com.example.FundSubscriptionFlow.Validator;

import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;

public interface InvestorDetailsValidator {

    void validate(Object details) throws InvestorTypeException;
}
