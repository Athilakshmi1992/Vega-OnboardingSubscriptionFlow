package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.RequestModel.FundDTO;
import com.example.FundSubscriptionFlow.Service.FundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for handling Fund-related operations.
 */
@RestController
@RequestMapping("/funds")
public class FundController {

    @Autowired
    private FundService fundService;

    /**
     * Creates a new Fund.
     *
     * @param fundDTO The DTO containing data to create the Fund.
     * @return ResponseEntity with the created FundDTO.
     */
    @Operation(summary = "Create a new fund", description = "Creates a new fund with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fund created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<FundDTO> createFund(
            @RequestBody FundDTO fundDTO) {
        FundDTO createdFund = fundService.createFund(fundDTO);
        return ResponseEntity.ok(createdFund);
    }

    /**
     * Updates an existing Fund.
     *
     * @param fundId  The ID of the Fund to update.
     * @param fundDTO The DTO containing updated data for the Fund.
     * @return ResponseEntity with the updated FundDTO.
     */
    @Operation(summary = "Update an existing fund", description = "Updates the details of an existing fund.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fund updated successfully"),
            @ApiResponse(responseCode = "404", description = "Fund not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{fundId}")
    public ResponseEntity<FundDTO> updateFund(
            @Parameter(description = "ID of the fund to be updated")
            @PathVariable UUID fundId,
            @RequestBody FundDTO fundDTO) {
        FundDTO updatedFund = fundService.updateFund(fundId, fundDTO);
        return ResponseEntity.ok(updatedFund);
    }

    /**
     * Retrieves a specific Fund by its ID.
     *
     * @param fundId The ID of the Fund to retrieve.
     * @return ResponseEntity with the FundDTO.
     */
    @Operation(summary = "Get fund by ID", description = "Retrieves the details of a specific fund by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fund retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Fund not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{fundId}")
    public ResponseEntity<FundDTO> getFund(
            @Parameter(description = "ID of the fund to be retrieved")
            @PathVariable UUID fundId) {
        FundDTO fundDTO = fundService.getFundById(fundId);
        return ResponseEntity.ok(fundDTO);
    }

    /**
     * Retrieves all Funds.
     *
     * @return ResponseEntity with a list of all FundDTOs.
     */
    @Operation(summary = "Get all funds", description = "Retrieves a list of all funds.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<FundDTO>> getAllFunds() {
        List<FundDTO> funds = fundService.getAllFunds();
        return ResponseEntity.ok(funds);
    }
}
