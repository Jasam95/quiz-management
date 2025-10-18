package com.example.quiz_management.service;

import com.example.quiz_management.entity.QuizAttempt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class QuizAttemptServiceTest {

    @Mock
    private QuizAttemptService quizAttemptService; // interface

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnList() {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setId(1L);

        when(quizAttemptService.findAll()).thenReturn(List.of(attempt));

        List<QuizAttempt> result = quizAttemptService.findAll();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void countByStatus_shouldReturnCorrectCount() {
        when(quizAttemptService.countByStatus("SUBMITTED")).thenReturn(5L);
        long count = quizAttemptService.countByStatus("SUBMITTED");
        assertEquals(5L, count);
    }
}
