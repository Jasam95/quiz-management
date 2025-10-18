package com.example.quiz_management.service.impl;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.service.QuizService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuizServiceImplementation implements QuizService {

    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }


    @Override
    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        Quiz existingQuiz= quizRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Quiz not found"));

            existingQuiz.setTitle(updatedQuiz.getTitle());
            existingQuiz.setDescription(updatedQuiz.getDescription());
            existingQuiz.setTotalMarks(updatedQuiz.getTotalMarks());
            existingQuiz.setDurationInMinutes(updatedQuiz.getDurationInMinutes());
            existingQuiz.setPassingScore(updatedQuiz.getPassingScore());
            existingQuiz.setMaxAttempts(updatedQuiz.getMaxAttempts());
            existingQuiz.setCreatedAt(LocalDateTime.now());
            return quizRepository.save(existingQuiz);
        }


    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    @Override
    public long count() {
        return quizRepository.count();
    }

    @Override
    public List<Quiz> findAll() {
        return quizRepository.findAll();
    }


    @Override
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }


}

