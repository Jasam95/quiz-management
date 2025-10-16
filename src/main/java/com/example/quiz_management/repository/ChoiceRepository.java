package com.example.quiz_management.repository;


import com.example.quiz_management.entity.Choice;
import com.example.quiz_management.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {

    List<Choice> findByQuestion(Question question);
}