package com.example.FundSubscriptionFlow.Service.ServiceImpl;

import com.example.FundSubscriptionFlow.Entity.Investor;
import com.example.FundSubscriptionFlow.Entity.InvestorType;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.example.FundSubscriptionFlow.Repository.InvestorRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorTypeRepository;
import com.example.FundSubscriptionFlow.Service.InvestorService;
import com.example.FundSubscriptionFlow.Validator.InvestorDetailsValidator;
import com.example.FundSubscriptionFlow.Validator.InvestorDetailsValidatorFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of InvestorService providing operations related to Investors.
 */
@Service
public class InvestorServiceImpl implements InvestorService {

    private static final Logger logger = LoggerFactory.getLogger(InvestorServiceImpl.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private InvestorTypeRepository investorTypeRepository;

    @Autowired
    private InvestorDetailsValidatorFactory investorDetailsValidatorFactory;

    /**
     * Creates a new Investor based on the provided details.
     *
     * @param typeId  The UUID of the InvestorType for the new Investor.
     * @param details The details of the Investor as JSON.
     * @return The newly created Investor.
     */
    @Override
    @Transactional
    public Investor createInvestor(UUID typeId, JsonNode details) {
        try {
            InvestorType investorType = investorTypeRepository.findById(typeId)
                    .orElseThrow(() -> new EntityNotFoundException("InvestorType not found with id: " + typeId));
            Optional.ofNullable(details)
                    .ifPresent(d -> {
                        try {
                            validateInvestorDetails(investorType.getType(), d);
                        } catch (InvestorTypeException e) {
                            throw new RuntimeException(e);
                        }
                    });
            Investor investor = Investor.builder().investorType(investorType).details(details!=null?details.toString():null).build();
            return investorRepository.save(investor);
        } catch (RuntimeException e) {
            logger.error("Error creating investor: ", e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating investor: ", e);
            throw new RuntimeException("Unexpected error creating investor");
        }
    }

    private void validateInvestorDetails(String investorType, Object details) throws InvestorTypeException {
        Object validateDetails = mapper.convertValue(details, Object.class);
        InvestorDetailsValidator validator = investorDetailsValidatorFactory.createValidator(investorType);
        try {
            validator.validate(validateDetails);
        } catch (InvestorTypeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves an Investor by its UUID.
     *
     * @param id The UUID of the Investor to retrieve.
     * @return The Investor.
     * @throws EntityNotFoundException If the Investor with the given id is not found.
     */
    @Override
    public Investor getInvestor(UUID id) {
        try {
            return investorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Investor not found with id: " + id));
        } catch (EntityNotFoundException e) {
            logger.error("Investor not found with id: " + id, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error retrieving investor with id: " + id, e);
            throw new RuntimeException("Unexpected error retrieving investor");
        }
    }

    /**
     * Retrieves all Investors.
     *
     * @return List of all Investors.
     * @throws RuntimeException If an unexpected error occurs while retrieving Investors.
     */
    @Override
    public List<Investor> getAllInvestors() {
        try {
            return investorRepository.findAll();
        } catch (Exception e) {
            logger.error("Unexpected error retrieving all investors: ", e);
            throw new RuntimeException("Unexpected error retrieving all investors");
        }
    }
}
