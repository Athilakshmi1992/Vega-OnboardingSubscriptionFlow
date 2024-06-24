package com.example.FundSubscriptionFlow;

import com.example.FundSubscriptionFlow.Entity.*;
import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorTypeRepository;
import com.example.FundSubscriptionFlow.Service.SubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private InvestorTypeRepository investorTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private InvestorRepository investorRepository;


    @Override
    public void run(String... args) throws Exception {
        // Create default funds
        Fund fund1 = Fund.builder().name("Sequoia Private Equity Fund").minimumInvestmentAmount(BigDecimal.valueOf(1000000.0)).build();
        Fund fund2 = Fund.builder().name("Coinbase Venture Capital Fund").minimumInvestmentAmount(BigDecimal.valueOf(500000.0)).build();

        fundRepository.saveAll(Arrays.asList(fund1, fund2));
        InvestorType individualInvestorType = InvestorType.builder().id(UUID.fromString("b9bc7c1c-4e3c-420e-ab85-230833d97d02")).type("Individual").build();
        InvestorType institutionalInvestorType = InvestorType.builder().id(UUID.fromString("7d008cda-004e-49e1-a653-22d30a2bca4c")).type("Institutional").build();
        investorTypeRepository.saveAll(Arrays.asList(individualInvestorType, institutionalInvestorType));

        // Create default investors
        Investor individualInvestor =
                Investor.builder().investorType(individualInvestorType).details(objectMapper.writeValueAsString(new IndividualInvestorDetails("James", "Smith", "UK"))).build();


        Investor institutionalInvestor = Investor.builder().investorType(institutionalInvestorType).details(objectMapper.writeValueAsString(new InstitutionalInvestorDetails("Oak Capital LLC", "USA", Arrays.asList(
                Director.builder().firstName("James").lastName("Smith").countryOfResidence("UK").build(), Director.builder().firstName("Richard").lastName("david").countryOfResidence("Canada").build())))).build();


        investorRepository.saveAll(Arrays.asList(individualInvestor, institutionalInvestor));


    }
}
