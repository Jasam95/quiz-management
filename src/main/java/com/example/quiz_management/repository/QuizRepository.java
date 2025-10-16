package com.example.quiz_management.repository;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    List<Quiz> findByAdmin(User admin);

    List<Quiz> findByTitleContainingIgnoreCase(String title);
}
