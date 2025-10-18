package com.example.quiz_management.dto;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.entity.UserAnswer;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class QuizAttemptDto {

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User participant;

    @Column(name = "started_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "submitted_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime submittedAt;

    @Column(length = 20)
    private String status = "IN_PROGRESS"; // IN_PROGRESS, SUBMITTED, GRADED

    private Double totalScore = 0.0;

    private Integer timeTakenSeconds = 0;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswer> userAnswers;
}
