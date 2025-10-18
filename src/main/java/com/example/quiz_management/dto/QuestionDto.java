package com.example.quiz_management.dto;

import com.example.quiz_management.entity.Choice;
import com.example.quiz_management.entity.Quiz;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionDto {

    @NotNull(message = "Question is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @NotNull(message = "Point is required")
    private Double points = 1.0; // default points for the question

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Each question belongs to a specific quiz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    // Each question has multiple choices
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices;
}
