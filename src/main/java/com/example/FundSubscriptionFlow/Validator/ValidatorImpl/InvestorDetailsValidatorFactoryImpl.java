package com.example.FundSubscriptionFlow.Validator.ValidatorImpl;

import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.example.FundSubscriptionFlow.Validator.IndividualInvestorDetailsValidator;
import com.example.FundSubscriptionFlow.Validator.InstitutionalInvestorDetailsValidator;
import com.example.FundSubscriptionFlow.Validator.InvestorDetailsValidator;
import com.example.FundSubscriptionFlow.Validator.InvestorDetailsValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of InvestorDetailsValidatorFactory that creates specific
 * validator instances based on investor type.
 */
@Component
public class InvestorDetailsValidatorFactoryImpl implements InvestorDetailsValidatorFactory {

    @Autowired
    InstitutionalInvestorDetailsValidator institutionalInvestorDetailsValidator;

    @Autowired
    IndividualInvestorDetailsValidator individualInvestorDetailsValidator;

    /**
     * Creates a validator instance based on the provided investor type.
     *
     * @param type The type of investor ("INDIVIDUAL" or "INSTITUTIONAL").
     * @return An instance of InvestorDetailsValidator corresponding to the investor type.
     * @throws InvestorTypeException if the provided investor type is unsupported.
     */
    @Override
    public InvestorDetailsValidator createValidator(String type) throws InvestorTypeException {
        switch (type.toUpperCase()) {
            case "INDIVIDUAL":
                return individualInvestorDetailsValidator;
            case "INSTITUTIONAL":
                return institutionalInvestorDetailsValidator;
            default:
                throw new InvestorTypeException("Unsupported investor type: " + type);
        }
    }
}
