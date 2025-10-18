package com.example.quiz_management.dto;

import com.example.quiz_management.entity.Question;
import com.example.quiz_management.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class QuizDto {
    @NotBlank(message = "Quiz title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @NotNull(message = "Total marks are required")
    private Integer totalMarks;

    @NotNull(message = "Duration (in minutes) is required")
    private Integer durationInMinutes; // minutes

    @NotNull(message = "Passing Score is required")
    private Double passingScore;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull(message = "Quiz needs how many time a student can attempts")
    @Min(value = 1, message = "Quiz need at least one attempt")
    private Integer maxAttempts = 1;

    // A quiz has many questions
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}
