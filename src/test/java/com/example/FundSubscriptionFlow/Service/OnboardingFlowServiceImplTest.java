package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.Entity.*;
import com.example.FundSubscriptionFlow.Repository.FundRepository;
import com.example.FundSubscriptionFlow.Repository.InvestorTypeRepository;
import com.example.FundSubscriptionFlow.Repository.OnboardingFlowRepository;
import com.example.FundSubscriptionFlow.Repository.TaskRepository;
import com.example.FundSubscriptionFlow.RequestModel.OnboardingFlowDTO;
import com.example.FundSubscriptionFlow.RequestModel.TaskDTO;
import com.example.FundSubscriptionFlow.Service.ServiceImpl.OnboardingFlowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class OnboardingFlowServiceImplTest {

    @Mock
    private OnboardingFlowRepository onboardingFlowRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private InvestorTypeRepository investorTypeRepository;

    @InjectMocks
    private OnboardingFlowServiceImpl onboardingFlowService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOnboardingFlow() {
        // Prepare
        OnboardingFlowDTO dto = createSampleOnboardingFlowDTO();
        OnboardingFlow onboardingFlow = createSampleOnboardingFlow();

        when(fundRepository.findById(dto.getFundId())).thenReturn(Optional.of(onboardingFlow.getFund()));
        when(investorTypeRepository.findById(dto.getInvestorTypeId())).thenReturn(Optional.of(onboardingFlow.getInvestorType()));
        when(taskRepository.findById(any(UUID.class))).thenAnswer((Answer<Optional<Task>>) invocation -> {
            UUID taskId = invocation.getArgument(0);
            return Optional.of(new Task(taskId,  List.of(Question.builder().text("Sample Task").build())));
        });
        when(onboardingFlowRepository.save(any(OnboardingFlow.class))).thenReturn(onboardingFlow);

        // Execute
        OnboardingFlowDTO createdFlowDTO = onboardingFlowService.createOnboardingFlow(dto);

        // Verify
        assertNotNull(createdFlowDTO);

        assertEquals(dto.getMinimumInvestment(), createdFlowDTO.getMinimumInvestment());
        assertEquals(dto.getTasks().size(), createdFlowDTO.getTasks().size());

        verify(fundRepository, times(1)).findById(dto.getFundId());
        verify(investorTypeRepository, times(1)).findById(dto.getInvestorTypeId());
        verify(taskRepository, times(dto.getTasks().size())).findById(any(UUID.class));
        verify(onboardingFlowRepository, times(1)).save(any(OnboardingFlow.class));
    }

    @Test
    public void testEditOnboardingFlow() {
        // Prepare
        UUID flowId = UUID.randomUUID();
        OnboardingFlowDTO updatedDto = createSampleOnboardingFlowDTO();
        updatedDto.setId(flowId);

        OnboardingFlow existingFlow = createSampleOnboardingFlow();
        existingFlow.setId(flowId);

        when(onboardingFlowRepository.findById(flowId)).thenReturn(Optional.of(existingFlow));
        when(fundRepository.findById(updatedDto.getFundId())).thenReturn(Optional.of(existingFlow.getFund()));
        when(investorTypeRepository.findById(updatedDto.getInvestorTypeId())).thenReturn(Optional.of(existingFlow.getInvestorType()));
        when(taskRepository.findById(any(UUID.class))).thenAnswer((Answer<Optional<Task>>) invocation -> {
            UUID taskId = invocation.getArgument(0);
            return Optional.of(new Task(taskId,  List.of(Question.builder().text("Sample Task").build())));
        });
        when(onboardingFlowRepository.save(any(OnboardingFlow.class))).thenReturn(existingFlow);

        // Execute
        OnboardingFlowDTO editedFlowDTO = onboardingFlowService.editOnboardingFlow(flowId, updatedDto);

        // Verify
        assertNotNull(editedFlowDTO);
        assertEquals(updatedDto.getId(), editedFlowDTO.getId());
        assertEquals(updatedDto.getMinimumInvestment(), editedFlowDTO.getMinimumInvestment());
        assertEquals(updatedDto.getTasks().size(), editedFlowDTO.getTasks().size());
        assertEquals(updatedDto.getTasks().get(0).getId(), editedFlowDTO.getTasks().get(0).getId());

        verify(onboardingFlowRepository, times(1)).findById(flowId);
        verify(fundRepository, times(1)).findById(updatedDto.getFundId());
        verify(investorTypeRepository, times(1)).findById(updatedDto.getInvestorTypeId());
        verify(taskRepository, times(updatedDto.getTasks().size())).findById(any(UUID.class));
        verify(onboardingFlowRepository, times(1)).save(any(OnboardingFlow.class));
    }

    @Test
    public void testGetAllOnboardingFlows() {
        // Prepare
        List<OnboardingFlow> onboardingFlows = new ArrayList<>();
        OnboardingFlow flow1 = createSampleOnboardingFlow();
        OnboardingFlow flow2 = createSampleOnboardingFlow();
        onboardingFlows.add(flow1);
        onboardingFlows.add(flow2);

        when(onboardingFlowRepository.findAll()).thenReturn(onboardingFlows);

        // Execute
        List<OnboardingFlowDTO> flowDTOs = onboardingFlowService.getAllOnboardingFlows();

        // Verify
        assertNotNull(flowDTOs);
        assertEquals(onboardingFlows.size(), flowDTOs.size());

        for (int i = 0; i < onboardingFlows.size(); i++) {
            assertEquals(onboardingFlows.get(i).getId(), flowDTOs.get(i).getId());
            assertEquals(onboardingFlows.get(i).getFund().getId(), flowDTOs.get(i).getFundId());
            assertEquals(onboardingFlows.get(i).getInvestorType().getId(), flowDTOs.get(i).getInvestorTypeId());
            assertEquals(onboardingFlows.get(i).getMinimumInvestment(), flowDTOs.get(i).getMinimumInvestment());
            assertEquals(onboardingFlows.get(i).getTasks().size(), flowDTOs.get(i).getTasks().size());
            assertEquals(onboardingFlows.get(i).getTasks().get(0).getId(), flowDTOs.get(i).getTasks().get(0).getId());
        }

        verify(onboardingFlowRepository, times(1)).findAll();
    }

    private OnboardingFlow createSampleOnboardingFlow() {
        Fund fund = new Fund(UUID.randomUUID(), "Sample Fund", BigDecimal.valueOf(1000.0));
        InvestorType investorType = new InvestorType(UUID.randomUUID(), "Individual");
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(UUID.randomUUID(),  List.of(Question.builder().text("Sample Task").build())));

        return new OnboardingFlow(UUID.randomUUID(), fund, investorType, BigDecimal.valueOf(500.0), tasks);
    }

    private OnboardingFlowDTO createSampleOnboardingFlowDTO() {
        TaskDTO taskDTO = new TaskDTO(UUID.randomUUID(), List.of(Question.builder().text("Sample Task").build()));
        List<TaskDTO> taskDTOs = new ArrayList<>();
        taskDTOs.add(taskDTO);

        return OnboardingFlowDTO.builder()
                .id(UUID.randomUUID())
                .fundId(UUID.randomUUID())
                .investorTypeId(UUID.randomUUID())
                .minimumInvestment(BigDecimal.valueOf(500.0))
                .tasks(taskDTOs)
                .build();
    }
}
