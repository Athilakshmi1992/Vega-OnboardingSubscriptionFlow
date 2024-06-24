package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.Entity.Investor;
import com.example.FundSubscriptionFlow.Entity.InvestorType;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.example.FundSubscriptionFlow.Repository.InvestorRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorTypeRepository;
import com.example.FundSubscriptionFlow.Service.ServiceImpl.InvestorServiceImpl;
import com.example.FundSubscriptionFlow.Validator.InvestorDetailsValidator;
import com.example.FundSubscriptionFlow.Validator.InvestorDetailsValidatorFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InvestorServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private InvestorTypeRepository investorTypeRepository;

    @Mock
    private InvestorDetailsValidatorFactory investorDetailsValidatorFactory;

    @InjectMocks
    private InvestorServiceImpl investorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvestor() throws InvestorTypeException {
        // Prepare
        UUID typeId = UUID.randomUUID();
        String investorType = "INDIVIDUAL";
        JsonNode details = objectMapper.valueToTree(Map.of("name", "John Doe", "age", 30));

        InvestorType mockInvestorType = new InvestorType(typeId, investorType);
        when(investorTypeRepository.findById(typeId)).thenReturn(Optional.of(mockInvestorType));

        InvestorDetailsValidator mockValidator = mock(InvestorDetailsValidator.class);
        when(investorDetailsValidatorFactory.createValidator(investorType)).thenReturn(mockValidator);

        Investor mockInvestor = new Investor(UUID.randomUUID(), mockInvestorType, details!=null?details.toString():null);
        when(investorRepository.save(any(Investor.class))).thenReturn(mockInvestor);

        // Execute
        Investor createdInvestor = investorService.createInvestor(typeId, details);

        // Verify
        assertNotNull(createdInvestor);
        assertEquals(mockInvestor.getId(), createdInvestor.getId());
        assertEquals(mockInvestor.getInvestorType(), createdInvestor.getInvestorType());
        assertEquals(mockInvestor.getDetails(), createdInvestor.getDetails());

        verify(investorTypeRepository, times(1)).findById(typeId);
        verify(investorRepository, times(1)).save(any(Investor.class));
    }

    @Test
    void testCreateInvestor_InvestorTypeNotFound() {
        // Prepare
        UUID typeId = UUID.randomUUID();
        JsonNode details = objectMapper.valueToTree(Map.of("name", "John Doe", "age", 30));

        when(investorTypeRepository.findById(typeId)).thenReturn(Optional.empty());

        // Execute and Verify
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> investorService.createInvestor(typeId, details));
        assertEquals("InvestorType not found with id: " + typeId, exception.getMessage());

        verify(investorTypeRepository, times(1)).findById(typeId);

    }



    @Test
    void testGetInvestor() {
        // Prepare
        UUID investorId = UUID.randomUUID();
        Investor mockInvestor = new Investor(investorId, new InvestorType(UUID.randomUUID(), "INDIVIDUAL"), "{}");
        when(investorRepository.findById(investorId)).thenReturn(Optional.of(mockInvestor));

        // Execute
        Investor retrievedInvestor = investorService.getInvestor(investorId);

        // Verify
        assertNotNull(retrievedInvestor);
        assertEquals(mockInvestor.getId(), retrievedInvestor.getId());
        assertEquals(mockInvestor.getInvestorType(), retrievedInvestor.getInvestorType());
        assertEquals(mockInvestor.getDetails(), retrievedInvestor.getDetails());

        verify(investorRepository, times(1)).findById(investorId);
    }

    @Test
    void testGetInvestor_NotFound() {
        // Prepare
        UUID nonExistingInvestorId = UUID.randomUUID();
        when(investorRepository.findById(nonExistingInvestorId)).thenReturn(Optional.empty());

        // Execute and Verify
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> investorService.getInvestor(nonExistingInvestorId));
        assertEquals("Investor not found with id: " + nonExistingInvestorId, exception.getMessage());

        verify(investorRepository, times(1)).findById(nonExistingInvestorId);
    }

    @Test
    void testGetAllInvestors() {
        // Prepare
        List<Investor> mockInvestors = Arrays.asList(
                new Investor(UUID.randomUUID(), new InvestorType(UUID.randomUUID(), "INDIVIDUAL"), "{}"),
                new Investor(UUID.randomUUID(), new InvestorType(UUID.randomUUID(), "INSTITUTIONAL"), "{}")
        );
        when(investorRepository.findAll()).thenReturn(mockInvestors);

        // Execute
        List<Investor> retrievedInvestors = investorService.getAllInvestors();

        // Verify
        assertNotNull(retrievedInvestors);
        assertEquals(mockInvestors.size(), retrievedInvestors.size());
        assertEquals(mockInvestors, retrievedInvestors);

        verify(investorRepository, times(1)).findAll();
    }
}
