package com.example.FundSubscriptionFlow.RequestModel;

import com.example.FundSubscriptionFlow.Entity.Question;
import com.example.FundSubscriptionFlow.Entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private UUID id;
    private List<Question> questions;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.questions = task.getQuestions();
    }

}
