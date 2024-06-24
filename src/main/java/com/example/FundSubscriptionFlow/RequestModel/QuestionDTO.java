package com.example.FundSubscriptionFlow.RequestModel;

import com.example.FundSubscriptionFlow.Entity.Question;
import java.util.UUID;

public class QuestionDTO {
    private UUID id;
    private String text;
    private boolean mandatory;

    public QuestionDTO() {
    }

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.text = question.getText();
        this.mandatory = question.isMandatory();
    }


}
