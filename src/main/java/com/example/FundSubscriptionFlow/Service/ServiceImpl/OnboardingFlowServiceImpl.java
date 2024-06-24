package com.example.FundSubscriptionFlow.Service.ServiceImpl;

import com.example.FundSubscriptionFlow.Entity.Fund;
import com.example.FundSubscriptionFlow.Entity.InvestorType;
import com.example.FundSubscriptionFlow.Entity.OnboardingFlow;
import com.example.FundSubscriptionFlow.Entity.Task;
import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorTypeRepository;
import com.example.FundSubscriptionFlow.Repository.OnboardingFlowRepository;
import com.example.FundSubscriptionFlow.Repository.TaskRepository;
import com.example.FundSubscriptionFlow.RequestModel.OnboardingFlowDTO;
import com.example.FundSubscriptionFlow.RequestModel.TaskDTO;
import com.example.FundSubscriptionFlow.Service.OnboardingFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for managing OnboardingFlow entities.
 */
@Service
@Transactional
public class OnboardingFlowServiceImpl implements OnboardingFlowService {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingFlowServiceImpl.class);

    private final OnboardingFlowRepository onboardingFlowRepository;
    private final FundRepository fundRepository;
    private final TaskRepository taskRepository;
    private final InvestorTypeRepository investorTypeRepository;

    @Autowired
    public OnboardingFlowServiceImpl(OnboardingFlowRepository onboardingFlowRepository,
                                     FundRepository fundRepository,
                                     TaskRepository taskRepository,
                                     InvestorTypeRepository investorTypeRepository) {
        this.onboardingFlowRepository = onboardingFlowRepository;
        this.fundRepository = fundRepository;
        this.taskRepository = taskRepository;
        this.investorTypeRepository = investorTypeRepository;
    }

    /**
     * Creates a new OnboardingFlow based on the provided DTO.
     *
     * @param onboardingFlowDTO The DTO containing data for the new OnboardingFlow.
     * @return The created OnboardingFlowDTO.
     */
    @Override
    public OnboardingFlowDTO createOnboardingFlow(OnboardingFlowDTO onboardingFlowDTO) {
        try {
            OnboardingFlow onboardingFlow = mapToEntity(onboardingFlowDTO);
            OnboardingFlow savedFlow = onboardingFlowRepository.save(onboardingFlow);
            return mapToDTO(savedFlow);
        } catch (Exception e) {
            logger.error("Failed to create OnboardingFlow: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    /**
     * Updates an existing OnboardingFlow identified by its ID.
     *
     * @param flowId         The ID of the OnboardingFlow to update.
     * @param updatedFlowDTO The DTO containing updated data for the OnboardingFlow.
     * @return The updated OnboardingFlowDTO.
     * @throws IllegalArgumentException If the OnboardingFlow with the given ID does not exist.
     */
    @Override
    public OnboardingFlowDTO editOnboardingFlow(UUID flowId, OnboardingFlowDTO updatedFlowDTO) {
        try {
            Optional<OnboardingFlow> optionalFlow = onboardingFlowRepository.findById(flowId);
            if (optionalFlow.isPresent()) {
                OnboardingFlow existingFlow = optionalFlow.get();
                updateEntity(existingFlow, updatedFlowDTO);
                OnboardingFlow updatedFlow = onboardingFlowRepository.save(existingFlow);
                return mapToDTO(updatedFlow);
            } else {
                String errorMessage = "Onboarding Flow not found with id: " + flowId;
                logger.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
        } catch (Exception e) {
            logger.error("Failed to update OnboardingFlow with id {}: {}", flowId, e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    /**
     * Retrieves all OnboardingFlows.
     *
     * @return A list of OnboardingFlowDTOs representing all OnboardingFlows.
     */
    @Override
    public List<OnboardingFlowDTO> getAllOnboardingFlows() {
        try {
            List<OnboardingFlow> onboardingFlows = onboardingFlowRepository.findAll();
            return onboardingFlows.stream().map(this::mapToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to retrieve all OnboardingFlows: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    /**
     * Maps an OnboardingFlowDTO to an OnboardingFlow entity.
     *
     * @param dto The DTO containing data to map.
     * @return The mapped OnboardingFlow entity.
     * @throws IllegalArgumentException If the associated Fund, InvestorType, or Task cannot be found.
     */
    private OnboardingFlow mapToEntity(OnboardingFlowDTO dto) {

        try {
            Fund fund = fundRepository.findById(dto.getFundId())
                    .orElseThrow(() -> new IllegalArgumentException("Fund not found with id: " + dto.getFundId()));
            InvestorType investorType = investorTypeRepository.findById(dto.getInvestorTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("InvestorType not found with id: " + dto.getInvestorTypeId()));
            List<Task> tasks = dto.getTasks().stream()
                    .map(taskDTO -> taskRepository.findById(taskDTO.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskDTO.getId())))
                    .collect(Collectors.toList());
            OnboardingFlow onboardingFlow = OnboardingFlow.builder().fund(fund).investorType(investorType).minimumInvestment(dto.getMinimumInvestment()).tasks(tasks).build();
            return onboardingFlow;
        } catch (Exception e) {
            logger.error("Failed to map OnboardingFlowDTO to entity: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    /**
     * Maps an OnboardingFlow entity to an OnboardingFlowDTO.
     *
     * @param entity The entity to map.
     * @return The mapped OnboardingFlowDTO.
     */
    private OnboardingFlowDTO mapToDTO(OnboardingFlow entity) {
        try {
            OnboardingFlowDTO dto = OnboardingFlowDTO.builder().id(entity.getId())
                    .fundId(entity.getFund().getId())
                    .investorTypeId(entity.getInvestorType().getId()).minimumInvestment(entity.getMinimumInvestment())
                    .tasks(entity.getTasks().stream().map(this::mapToDTO).collect(Collectors.toList())).build();
            return dto;
        } catch (Exception e) {
            logger.error("Failed to map OnboardingFlow entity to DTO: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    /**
     * Maps a Task entity to a TaskDTO.
     *
     * @param entity The Task entity to map.
     * @return The mapped TaskDTO.
     */
    private TaskDTO mapToDTO(Task entity) {
        try {
            TaskDTO dto = TaskDTO.builder().id(entity.getId()).questions(entity.getQuestions()).build();
            return dto;
        } catch (Exception e) {
            logger.error("Failed to map Task entity to DTO: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }

    /**
     * Updates an existing OnboardingFlow entity with data from the DTO.
     *
     * @param entity The OnboardingFlow entity to update.
     * @param dto    The DTO containing updated data.
     * @throws IllegalArgumentException If the associated Fund, InvestorType, or Task cannot be found.
     */
    private void updateEntity(OnboardingFlow entity, OnboardingFlowDTO dto) {
        try {
            if (dto.getFundId() != null) {
                Fund fund = fundRepository.findById(dto.getFundId())
                        .orElseThrow(() -> new IllegalArgumentException("Fund not found with id: " + dto.getFundId()));
                entity.setFund(fund);
            }
            if (dto.getInvestorTypeId() != null) {
                InvestorType investorType = investorTypeRepository.findById(dto.getInvestorTypeId())
                        .orElseThrow(() -> new IllegalArgumentException("InvestorType not found with id: " + dto.getInvestorTypeId()));
                entity.setInvestorType(investorType);
            }
            if (dto.getMinimumInvestment() != null) {
                entity.setMinimumInvestment(dto.getMinimumInvestment());
            }
            if (dto.getTasks() != null && !dto.getTasks().isEmpty()) {
                List<Task> tasks = dto.getTasks().stream()
                        .map(taskDTO -> taskRepository.findById(taskDTO.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskDTO.getId())))
                        .collect(Collectors.toList());
                entity.setTasks(tasks);
            } else {
                entity.setTasks(new ArrayList<>());
            }
        } catch (Exception e) {
            logger.error("Failed to update OnboardingFlow entity: {}", e.getMessage());
            throw e; // Rethrow the exception for global exception handling
        }
    }
}
