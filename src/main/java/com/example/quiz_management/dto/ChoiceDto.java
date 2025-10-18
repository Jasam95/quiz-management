package com.example.quiz_management.dto;

import com.example.quiz_management.entity.Question;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

public class ChoiceDto {

    @NotNull(message = "Option must be entered")
    @Column(nullable = false, length = 1000)

    private String text;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
