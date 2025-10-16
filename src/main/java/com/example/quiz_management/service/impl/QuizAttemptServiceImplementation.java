package com.example.quiz_management.service.impl;

import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.repository.QuizAttemptRepository;
import com.example.quiz_management.service.QuizAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuizAttemptServiceImplementation implements QuizAttemptService {

    private QuizAttemptRepository quizAttemptRepository;

    @Override
    public void save(QuizAttempt attempt) {
        quizAttemptRepository.save(attempt);
    }

    @Override
    public List<QuizAttempt> findAllByParticipant(User participant) {
        return quizAttemptRepository.findByParticipant(participant);
    }

    @Override
    public List<QuizAttempt> findAll() {
        return quizAttemptRepository.findAll();
    }

    @Override
    public long count() {
        return quizAttemptRepository.count();
    }

    @Override
    public long countByStatus(String submitted) {
        return quizAttemptRepository.countByStatus(submitted);
    }

    @Override
    public List<QuizAttempt> findTop5ByOrderByStartedAtDesc() {
        return quizAttemptRepository.findTop5ByOrderByStartedAtDesc();
    }
}
