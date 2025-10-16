package com.example.quiz_management.service;

import com.example.quiz_management.entity.Question;
import com.example.quiz_management.entity.Quiz;

import java.util.List;
import java.util.Optional;


public interface QuestionService {
    List<Question> getAllQuestions();

    Optional<Question> findById(Long id);

    Question save(Question question);

    void updateQuestion(Long id, Question question);

    void deleteQuestion(Long id);
}
