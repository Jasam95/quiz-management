package com.example.quiz_management.entity;





import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

