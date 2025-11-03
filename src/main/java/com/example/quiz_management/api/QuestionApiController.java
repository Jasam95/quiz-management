package com.example.quiz_management.api;

import com.example.quiz_management.entity.Question;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@Tag(name = "Question API", description = "Manage quiz questions and choices")
public class QuestionApiController {

    private final QuestionService questionService;
    private final QuizRepository quizRepository;



    // Create a new question with choices
    @PostMapping
    @Operation(summary = "Create a new question", description = "Add a question with its multiple choices and associated quiz ID")
    public ResponseEntity<?> saveQuestion(@RequestBody Question question) {
        try {
            Question saved = questionService.save(question);
            return ResponseEntity.ok(Map.of(
                    "message", "Question saved successfully",
                    "id", saved.getId()
            ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Duplicate or invalid question data",
                    "details", e.getMessage()
            ));
        }
    }

    //  Delete a question
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a question", description = "Remove a question and its choices by ID")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(Map.of("message", "🗑️ Question deleted successfully"));
    }


    // get all Questions
    @GetMapping
    @Operation(summary = "List all questions", description = "Fetch all questions with their associated quiz and choices")
    public ResponseEntity<List<Map<String, Object>>> listQuestions() {
        List<Map<String, Object>> questions = questionService.getAllQuestions().stream()
                .map(q -> {
                    Map<String, Object> questionMap = new LinkedHashMap<>();
                    questionMap.put("id", q.getId());
                    questionMap.put("text", q.getQuestionText());
                    questionMap.put("quizTitle", q.getQuiz() != null ? q.getQuiz().getTitle() : null);

                    List<Map<String, Object>> choices = q.getChoices().stream()
                            .map(c -> {
                                Map<String, Object> choiceMap = new HashMap<>();
                                choiceMap.put("id", c.getId());
                                choiceMap.put("text", c.getText());
                                choiceMap.put("correct", c.getIsCorrect());
                                return choiceMap;
                            })
                            .toList();

                    questionMap.put("choices", choices);
                    return questionMap;
                })
                .toList();

        return ResponseEntity.ok(questions);
    }
    //get all quizzes
    @GetMapping("/quizzes")
    @Operation(summary = "Get all quizzes", description = "Fetch quizzes to assign to new questions")
    public ResponseEntity<List<Map<String, Object>>> getAllQuizzes() {
        var quizzes = quizRepository.findAll().stream()
                .map(q -> {
                    Map<String, Object> quizMap = new HashMap<>();
                    quizMap.put("id", q.getId());
                    quizMap.put("title", q.getTitle());
                    quizMap.put("description", q.getDescription());
                    return quizMap;
                })
                .toList();
        return ResponseEntity.ok(quizzes);
    }



}
