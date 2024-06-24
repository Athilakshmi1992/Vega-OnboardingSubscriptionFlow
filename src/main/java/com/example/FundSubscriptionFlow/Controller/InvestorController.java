package com.example.FundSubscriptionFlow.Controller;

import com.example.FundSubscriptionFlow.Entity.Investor;
import com.example.FundSubscriptionFlow.Exception.InvestorTypeException;
import com.example.FundSubscriptionFlow.RequestModel.InvestorDTO;
import com.example.FundSubscriptionFlow.Service.InvestorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    private static final Logger logger = LoggerFactory.getLogger(InvestorController.class);

    @Autowired
    private InvestorService investorService;

    @Operation(summary = "Create a new investor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Investor created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Investor> createInvestor(
            @Parameter(description = "Investor details") @RequestBody InvestorDTO investorDTO) {
        try {
            Investor createdInvestor = investorService.createInvestor(investorDTO.getType(), investorDTO.getDetails());
            return new ResponseEntity<>(createdInvestor, HttpStatus.CREATED);
        } catch (JsonProcessingException | InvestorTypeException e) {
            logger.error("Error creating investor", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error creating investor", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get an investor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investor found", content = @Content),
            @ApiResponse(responseCode = "404", description = "Investor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{investorId}")
    public ResponseEntity<Investor> getInvestor(
            @Parameter(description = "ID of the investor to be retrieved") @PathVariable UUID investorId) {
        try {
            Investor investor = investorService.getInvestor(investorId);
            return new ResponseEntity<>(investor, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Investor not found with id: " + investorId, e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving investor with id: " + investorId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get all investors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investors retrieved successfully", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Investor>> getAllInvestors() {
        try {
            List<Investor> investors = investorService.getAllInvestors();
            return new ResponseEntity<>(investors, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unexpected error retrieving all investors", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
