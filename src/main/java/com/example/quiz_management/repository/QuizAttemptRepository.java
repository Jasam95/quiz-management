package com.example.quiz_management.repository;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByParticipant(User participant);

    List<QuizAttempt> findByQuizAndParticipant(Quiz quiz, User participant);

    Optional<QuizAttempt>  findByQuizAndParticipantAndStatus(Quiz quiz, User participant, String status );

    List<QuizAttempt> findByQuiz(Quiz quiz);

    long countByStatus(String status);

    List<QuizAttempt> findTop5ByOrderByStartedAtDesc();
}
