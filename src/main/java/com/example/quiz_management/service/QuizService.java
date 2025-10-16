package com.example.quiz_management.service;

import com.example.quiz_management.entity.Quiz;

import java.util.List;

public interface QuizService {
    List<Quiz> getAllQuizzes();

    Quiz getQuizById(Long id);
    Quiz saveQuiz(Quiz quiz);
    Quiz updateQuiz(Long id, Quiz updatedQuiz);
    void deleteQuiz(Long id);

    long count();
}
