package com.example.quiz_management.service;

import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;

import java.util.List;
import java.util.Map;

public interface QuizAttemptService {
    void save(QuizAttempt attempt);

    List<QuizAttempt> findAllByParticipant(User participant);

    List<QuizAttempt> findAll();

    long count();

    long countByStatus(String submitted);

    List<QuizAttempt> findTop5ByOrderByStartedAtDesc();

    QuizAttempt startOrResumeAttempt(Long quizId, User participant);

    QuizAttempt submitAttempt(Long quizId, User participant, Map<String, String> allParams);

}
