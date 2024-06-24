package com.example.FundSubscriptionFlow.Validator;

import com.example.FundSubscriptionFlow.Entity.InvestorType;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;

public interface InvestorDetailsValidatorFactory {
    InvestorDetailsValidator createValidator(String investorType) throws InvestorTypeException;
}
