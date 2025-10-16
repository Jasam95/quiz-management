package com.example.quiz_management.service;

import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;

import java.util.List;

public interface QuizAttemptService {
    void save(QuizAttempt attempt);

    List<QuizAttempt> findAllByParticipant(User participant);

    List<QuizAttempt> findAll();

    long count();

    long countByStatus(String submitted);

    List<QuizAttempt> findTop5ByOrderByStartedAtDesc();
}
