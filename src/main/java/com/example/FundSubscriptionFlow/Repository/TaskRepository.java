package com.example.FundSubscriptionFlow.Repository;

import com.example.FundSubscriptionFlow.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface TaskRepository extends JpaRepository<Task, UUID> {
}
