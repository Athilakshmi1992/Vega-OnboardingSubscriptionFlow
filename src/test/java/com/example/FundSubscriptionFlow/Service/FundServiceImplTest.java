package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.Entity.Fund;
import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.RequestModel.FundDTO;
import com.example.FundSubscriptionFlow.Service.FundService;
import com.example.FundSubscriptionFlow.Service.ServiceImpl.FundServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FundServiceImplTest {

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundServiceImpl fundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFund() {
        // Prepare
        FundDTO fundDTO = new FundDTO("Test Fund", new BigDecimal("1000"));
        Fund savedFund = new Fund(UUID.randomUUID(), fundDTO.getName(), fundDTO.getMinimumInvestmentAmount());
        when(fundRepository.save(any(Fund.class))).thenReturn(savedFund);

        // Execute
        FundDTO createdFund = fundService.createFund(fundDTO);

        // Verify
        assertNotNull(createdFund);
        assertEquals(fundDTO.getName(), createdFund.getName());
        assertEquals(fundDTO.getMinimumInvestmentAmount(), createdFund.getMinimumInvestmentAmount());

        verify(fundRepository, times(1)).save(any(Fund.class));
    }

    @Test
    void testUpdateFund() {
        // Prepare
        UUID fundId = UUID.randomUUID();
        FundDTO fundDTO = new FundDTO("Updated Fund", new BigDecimal("2000"));
        Fund existingFund = new Fund(fundId, "Old Fund", new BigDecimal("1500"));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(existingFund));
        when(fundRepository.save(any(Fund.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute
        FundDTO updatedFund = fundService.updateFund(fundId, fundDTO);

        // Verify
        assertNotNull(updatedFund);
        assertEquals(existingFund.getId(), updatedFund.getId());
        assertEquals(fundDTO.getName(), updatedFund.getName());
        assertEquals(fundDTO.getMinimumInvestmentAmount(), updatedFund.getMinimumInvestmentAmount());

        verify(fundRepository, times(1)).findById(fundId);
        verify(fundRepository, times(1)).save(any(Fund.class));
    }

    @Test
    void testUpdateFund_NotFound() {
        // Prepare
        UUID nonExistingFundId = UUID.randomUUID();
        FundDTO fundDTO = new FundDTO("Updated Fund", new BigDecimal("2000"));
        when(fundRepository.findById(nonExistingFundId)).thenReturn(Optional.empty());

        // Execute and Verify
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundService.updateFund(nonExistingFundId, fundDTO));
        assertEquals("Fund not found with id: " + nonExistingFundId, exception.getMessage());

        verify(fundRepository, times(1)).findById(nonExistingFundId);
        verify(fundRepository, never()).save(any(Fund.class));
    }

    @Test
    void testGetFundById() {
        // Prepare
        UUID fundId = UUID.randomUUID();
        Fund existingFund = new Fund(fundId, "Test Fund", new BigDecimal("1000"));
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(existingFund));

        // Execute
        FundDTO retrievedFund = fundService.getFundById(fundId);

        // Verify
        assertNotNull(retrievedFund);
        assertEquals(existingFund.getId(), retrievedFund.getId());
        assertEquals(existingFund.getName(), retrievedFund.getName());
        assertEquals(existingFund.getMinimumInvestmentAmount(), retrievedFund.getMinimumInvestmentAmount());

        verify(fundRepository, times(1)).findById(fundId);
    }

    @Test
    void testGetFundById_NotFound() {
        // Prepare
        UUID nonExistingFundId = UUID.randomUUID();
        when(fundRepository.findById(nonExistingFundId)).thenReturn(Optional.empty());

        // Execute and Verify
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> fundService.getFundById(nonExistingFundId));
        assertEquals("Fund not found with id: " + nonExistingFundId, exception.getMessage());

        verify(fundRepository, times(1)).findById(nonExistingFundId);
    }

    @Test
    void testGetAllFunds() {
        // Prepare
        List<Fund> funds = Arrays.asList(
                new Fund(UUID.randomUUID(), "Fund 1", new BigDecimal("1000")),
                new Fund(UUID.randomUUID(), "Fund 2", new BigDecimal("2000"))
        );
        when(fundRepository.findAll()).thenReturn(funds);

        // Execute
        List<FundDTO> allFunds = fundService.getAllFunds();

        // Verify
        assertNotNull(allFunds);
        assertEquals(funds.size(), allFunds.size());

        // Check if all funds are correctly mapped
        for (int i = 0; i < funds.size(); i++) {
            Fund expectedFund = funds.get(i);
            FundDTO actualFund = allFunds.get(i);
            assertEquals(expectedFund.getId(), actualFund.getId());
            assertEquals(expectedFund.getName(), actualFund.getName());
            assertEquals(expectedFund.getMinimumInvestmentAmount(), actualFund.getMinimumInvestmentAmount());
        }

        verify(fundRepository, times(1)).findAll();
    }
}
