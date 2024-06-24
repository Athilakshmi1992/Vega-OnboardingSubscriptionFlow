package com.example.FundSubscriptionFlow.Service;

import com.example.FundSubscriptionFlow.RequestModel.TaskDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing tasks.
 */
public interface TaskService {

    /**
     * Creates a new task based on the provided TaskDTO.
     *
     * @param taskDTO The TaskDTO containing task details.
     * @return The created TaskDTO.
     */
    TaskDTO createTask(TaskDTO taskDTO);

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param id The UUID of the task to retrieve.
     * @return The TaskDTO corresponding to the given id.
     */
    TaskDTO getTask(UUID id);

    /**
     * Retrieves all tasks available in the system.
     *
     * @return A list of all TaskDTOs.
     */
    List<TaskDTO> getAllTasks();
}
