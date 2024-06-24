package com.example.FundSubscriptionFlow.Service.ServiceImpl;

import com.example.FundSubscriptionFlow.Entity.Task;
import com.example.FundSubscriptionFlow.Repository.TaskRepository;
import com.example.FundSubscriptionFlow.RequestModel.TaskDTO;
import com.example.FundSubscriptionFlow.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of TaskService interface.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Creates a new task based on the provided TaskDTO.
     *
     * @param taskDTO The TaskDTO containing task details to be created.
     * @return The created TaskDTO.
     */
    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = Task.builder().questions(taskDTO.getQuestions()).build();
        task = taskRepository.save(task);
        taskDTO.setId(task.getId());
        return taskDTO;
    }

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The UUID of the task to retrieve.
     * @return The TaskDTO corresponding to the given id.
     * @throws RuntimeException if the task with the specified id is not found.
     */
    @Override
    public TaskDTO getTask(UUID id) {
        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
            return new TaskDTO(task);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get task with id: " + id, e);
        }
    }

    /**
     * Retrieves all tasks available in the system.
     *
     * @return A list of all TaskDTOs.
     * @throws RuntimeException if there is an error retrieving tasks.
     */
    @Override
    public List<TaskDTO> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            return tasks.stream()
                    .map(TaskDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all tasks", e);
        }
    }
}
