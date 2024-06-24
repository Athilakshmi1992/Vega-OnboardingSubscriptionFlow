package com.example.FundSubscriptionFlow.Service.ServiceImpl;

import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.Entity.Fund;
import com.example.FundSubscriptionFlow.RequestModel.FundDTO;
import com.example.FundSubscriptionFlow.Service.FundService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Funds.
 */
@Service
public class FundServiceImpl implements FundService {

    private static final Logger logger = LoggerFactory.getLogger(FundServiceImpl.class);

    @Autowired
    private FundRepository fundRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FundDTO createFund(FundDTO fundDTO) {
        try {
            Fund fund = Fund.builder()
                    .name(fundDTO.getName())
                    .minimumInvestmentAmount(fundDTO.getMinimumInvestmentAmount())
                    .build();
            fundRepository.save(fund);
            return mapToDTO(fund);
        } catch (Exception e) {
            logger.error("Error creating fund", e);
            throw new RuntimeException("Error creating fund: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FundDTO updateFund(UUID fundId, FundDTO fundDTO) {
        try {
            Fund fund = fundRepository.findById(fundId)
                    .orElseThrow(() -> new EntityNotFoundException("Fund not found with id: " + fundId));
            fund.setName(fundDTO.getName());
            fund.setMinimumInvestmentAmount(fundDTO.getMinimumInvestmentAmount());
            fundRepository.save(fund);
            return mapToDTO(fund);
        } catch (EntityNotFoundException e) {
            logger.error("Fund not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating fund", e);
            throw new RuntimeException("Error updating fund: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FundDTO getFundById(UUID fundId) {
        try {
            Fund fund = fundRepository.findById(fundId)
                    .orElseThrow(() -> new EntityNotFoundException("Fund not found with id: " + fundId));
            return mapToDTO(fund);
        } catch (EntityNotFoundException e) {
            logger.error("Fund not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving fund", e);
            throw new RuntimeException("Error retrieving fund: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FundDTO> getAllFunds() {
        try {
            return fundRepository.findAll().stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all funds", e);
            throw new RuntimeException("Error retrieving all funds: " + e.getMessage(), e);
        }
    }

    private FundDTO mapToDTO(Fund fund) {
        return new FundDTO(fund.getId(), fund.getName(), fund.getMinimumInvestmentAmount());
    }
}
