package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.RequestModel.FundDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing Funds.
 */
@Service
public interface FundService {

    /**
     * Creates a new Fund.
     *
     * @param fundDTO The DTO containing data to create the Fund.
     * @return The created FundDTO.
     */
    FundDTO createFund(FundDTO fundDTO);

    /**
     * Updates an existing Fund.
     *
     * @param fundId  The ID of the Fund to update.
     * @param fundDTO The DTO containing updated data for the Fund.
     * @return The updated FundDTO.
     */
    FundDTO updateFund(UUID fundId, FundDTO fundDTO);

    /**
     * Retrieves a specific Fund by its ID.
     *
     * @param fundId The ID of the Fund to retrieve.
     * @return The FundDTO corresponding to the specified ID.
     */
    FundDTO getFundById(UUID fundId);

    /**
     * Retrieves all Funds.
     *
     * @return A list of all FundDTOs.
     */
    List<FundDTO> getAllFunds();
}
