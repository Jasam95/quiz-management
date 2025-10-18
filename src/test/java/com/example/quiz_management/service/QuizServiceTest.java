package com.example.quiz_management.service;


import com.example.quiz_management.entity.Quiz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class QuizServiceTest {

    @Mock
    private QuizService quizService; // interface

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnListOfQuizzes() {
        Quiz q = new Quiz();
        q.setId(1L);
        q.setTitle("Java Quiz");

        when(quizService.findAll()).thenReturn(List.of(q));

        List<Quiz> result = quizService.findAll();

        assertEquals(1, result.size());
        assertEquals("Java Quiz", result.getFirst().getTitle());
    }

    @Test
    void findById_shouldReturnQuizIfExists() {
        Quiz q = new Quiz();
        q.setId(1L);
        q.setTitle("Spring Quiz");

        when(quizService.getQuizById(1L)).thenReturn(q);

        Quiz result = quizService.getQuizById(1L);

        assertNotNull(result);
        assertEquals("Spring Quiz", result.getTitle());
    }
}

