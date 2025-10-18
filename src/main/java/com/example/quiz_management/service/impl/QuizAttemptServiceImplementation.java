package com.example.quiz_management.service.impl;

import com.example.quiz_management.entity.*;
import com.example.quiz_management.repository.QuizAttemptRepository;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.service.QuizAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class QuizAttemptServiceImplementation implements QuizAttemptService {

    private QuizAttemptRepository quizAttemptRepository;

    private final QuizRepository quizRepository;

    @Override
    public void save(QuizAttempt attempt) {
        quizAttemptRepository.save(attempt);
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

    @Override
    public QuizAttempt startOrResumeAttempt(Long quizId, User participant) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return quizAttemptRepository
                .findByQuizAndParticipantAndStatus(quiz, participant, "IN_PROGRESS")
                .orElseGet(() -> {
                    QuizAttempt newAttempt = new QuizAttempt();
                    newAttempt.setQuiz(quiz);
                    newAttempt.setParticipant(participant);
                    newAttempt.setStatus("IN_PROGRESS");
                    newAttempt.setStartedAt(LocalDateTime.now());
                    return quizAttemptRepository.save(newAttempt);
                });
    }



    @Override
    public QuizAttempt submitAttempt(Long quizId, User participant, Map<String, String> allParams) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        QuizAttempt attempt = quizAttemptRepository
                .findByQuizAndParticipantAndStatus(quiz, participant, "IN_PROGRESS")
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (attempt.getUserAnswers() == null) {
            attempt.setUserAnswers(new ArrayList<>());
        } else {
            attempt.getUserAnswers().clear();
        }

        double totalScore = 0.0;

        for (Question question : quiz.getQuestions()) {
            String paramName = "selectedChoiceId_" + question.getId();
            String selectedChoiceIdStr = allParams.get(paramName);

            UserAnswer answer = new UserAnswer();
            answer.setQuizAttempt(attempt);
            answer.setQuestion(question);

            if (selectedChoiceIdStr != null && !selectedChoiceIdStr.isEmpty()) {
                Long selectedChoiceId = Long.valueOf(selectedChoiceIdStr);
                Choice selectedChoice = question.getChoices().stream()
                        .filter(c -> c.getId().equals(selectedChoiceId))
                        .findFirst()
                        .orElse(null);

                answer.setSelectedChoiceIds(selectedChoiceIdStr);
                if (selectedChoice != null && selectedChoice.getIsCorrect()) {
                    answer.setIsCorrect(true);
                    totalScore += question.getPoints();
                    answer.setPointsForEachAnswer(question.getPoints());
                } else {
                    answer.setIsCorrect(false);
                }
            } else {
                answer.setIsCorrect(false);
            }

            attempt.getUserAnswers().add(answer);
        }

        attempt.setTotalScore(totalScore);
        attempt.setStatus("SUBMITTED");
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setTimeTakenSeconds((int)
                Duration.between(attempt.getStartedAt(), attempt.getSubmittedAt()).getSeconds()
        );

        return quizAttemptRepository.save(attempt);
    }

    @Override
    public List<QuizAttempt> findAllByParticipant(User participant) {
        return quizAttemptRepository.findByParticipant(participant);
    }

}
