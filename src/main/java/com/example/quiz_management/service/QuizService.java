package com.example.quiz_management.service;

import com.example.quiz_management.entity.Quiz;

import java.util.List;

public interface QuizService {
    List<Quiz> getAllQuizzes();

    Quiz getQuizById(Long id);
    void saveQuiz(Quiz quiz);
    void updateQuiz(Long id, Quiz updatedQuiz);
    void deleteQuiz(Long id);

    long count();

    List<Quiz> findAll();

}
