package com.example.quiz_management.repository;



import com.example.quiz_management.entity.UserAnswer;
import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByQuizAttempt(QuizAttempt quizAttempt);

    List<UserAnswer> findByQuizAttemptAndQuestion(QuizAttempt quizAttempt, Question question);
}

