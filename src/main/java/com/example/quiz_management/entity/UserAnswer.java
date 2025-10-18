package com.example.quiz_management.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attempt_id")
    private QuizAttempt quizAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "selected_choice_ids", columnDefinition = "TEXT")
    private String selectedChoiceIds; // store as comma-separated or JSON string

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @Column(name = "pointsForEachAnswer")
    private Double pointsForEachAnswer = 0.0;
}

