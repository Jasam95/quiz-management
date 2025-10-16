package com.example.quiz_management.service.impl;

import com.example.quiz_management.entity.Choice;
import com.example.quiz_management.entity.Question;
import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.repository.QuestionRepository;
import com.example.quiz_management.service.QuestionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuestionServiceImplementation implements QuestionService {
    private QuestionRepository questionRepository;


    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question save(Question question) {

            // Validate that there are choices
            if (question.getChoices() == null || question.getChoices().isEmpty()) {
                throw new IllegalArgumentException("A question must have at least one choice.");
            }

            for (Choice choice : question.getChoices()) {
                choice.setQuestion(question);
            }

            try {
                return questionRepository.save(question);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Database error while saving question: " + e.getMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException("Unexpected error occurred while saving question.", e);
            }
        }


    @Override
    @Transactional
    public void updateQuestion(Long id, Question updatedQuestion) {
        Question existingQuestion= questionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Question not found"));

        Map<Long, Choice> existingChoiceMap = existingQuestion.getChoices().stream()
                .collect(Collectors.toMap(Choice::getId, c -> c));

        for (Choice updatedChoice : updatedQuestion.getChoices()) {
            if (updatedChoice.getId() != null &&
                    existingChoiceMap.containsKey(updatedChoice.getId())) {
                // Update existing choice
                Choice existingChoice = existingChoiceMap.get(updatedChoice.getId());
                existingChoice.setText(updatedChoice.getText());
                existingChoice.setIsCorrect(updatedChoice.getIsCorrect());
            } else {
                // New choice added (no ID)
                updatedChoice.setQuestion(existingQuestion);
                existingQuestion.getChoices().add(updatedChoice);
            }
        }

        // Remove choices that are missing in the new list
        existingQuestion.getChoices().removeIf(
                old -> updatedQuestion.getChoices().stream()
                        .noneMatch(newChoice -> newChoice.getId() != null && newChoice.getId().equals(old.getId()))
        );

        questionRepository.save(existingQuestion);
    }

    @Override
    public void deleteQuestion(Long id) {
            questionRepository.deleteById(id);
    }
}
