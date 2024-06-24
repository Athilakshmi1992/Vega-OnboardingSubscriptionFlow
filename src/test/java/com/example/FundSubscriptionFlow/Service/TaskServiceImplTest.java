package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.Entity.Question;
import com.example.FundSubscriptionFlow.Entity.Task;
import com.example.FundSubscriptionFlow.Repository.TaskRepository;
import com.example.FundSubscriptionFlow.RequestModel.TaskDTO;
import com.example.FundSubscriptionFlow.Service.ServiceImpl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask() {
        // Mock data
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setQuestions(List.of(Question.builder().text("Sample question").build()));

        Task savedTask = new Task(UUID.randomUUID(), List.of(Question.builder().text("Sample question").build()));

        // Mock repository behavior
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Call the service method
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        // Verify repository method was called with correct data
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(taskCaptor.capture());

        Task capturedTask = taskCaptor.getValue();
        assertEquals(taskDTO.getQuestions(), capturedTask.getQuestions());

        // Verify returned DTO
        assertEquals(savedTask.getId(), createdTaskDTO.getId());
        assertEquals(taskDTO.getQuestions(), createdTaskDTO.getQuestions());
    }

    @Test
    public void testGetTask() {
        // Mock data
        UUID taskId = UUID.randomUUID();
        Task mockTask = new Task(taskId, List.of(Question.builder().text("Sample question").build()));

        // Mock repository behavior
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        // Call the service method
        TaskDTO taskDTO = taskService.getTask(taskId);

        // Verify repository method was called with correct ID
        verify(taskRepository, times(1)).findById(taskId);

        // Verify returned DTO
        assertEquals(taskId, taskDTO.getId());
        assertEquals(mockTask.getQuestions(), taskDTO.getQuestions());
    }

    @Test
    public void testGetTask_NotFound() {
        // Mock data
        UUID taskId = UUID.randomUUID();

        // Mock repository behavior
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        assertThrows(RuntimeException.class, () -> {
            taskService.getTask(taskId);
        });

        // Verify repository method was called with correct ID
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void testGetAllTasks() {
        // Mock data
        Task task1 = new Task(UUID.randomUUID(), List.of(Question.builder().text("Sample question").build()));
        Task task2 = new Task(UUID.randomUUID(), List.of(Question.builder().text("Sample question").build()));
        List<Task> mockTasks = Arrays.asList(task1, task2);

        // Mock repository behavior
        when(taskRepository.findAll()).thenReturn(mockTasks);

        // Call the service method
        List<TaskDTO> taskDTOs = taskService.getAllTasks();

        // Verify repository method was called
        verify(taskRepository, times(1)).findAll();

        // Verify returned DTOs
        assertEquals(mockTasks.size(), taskDTOs.size());
        for (int i = 0; i < mockTasks.size(); i++) {
            assertEquals(mockTasks.get(i).getId(), taskDTOs.get(i).getId());
            assertEquals(mockTasks.get(i).getQuestions(), taskDTOs.get(i).getQuestions());
        }
    }
}
