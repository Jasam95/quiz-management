package com.example.quiz_management.api;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.Question;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.repository.QuestionRepository;
import com.example.quiz_management.repository.UserRepository;
import com.example.quiz_management.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quizzes")
@Tag(name = "Quiz Management API", description = "Endpoints for managing quizzes, questions and attempts")
public class QuizApiController {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizService quizService;

    // Get all quizzes
    @GetMapping
    @Operation(summary = "Get all quizzes", description = "Fetch all available quizzes in simplified JSON format")
    public ResponseEntity<List<Map<String, Object>>> getAllQuizzes() {
        List<Map<String, Object>> quizzes = quizRepository.findAll()
                .stream()
                .map(q -> {
                    Map<String, Object> quizMap = new HashMap<>();
                    quizMap.put("id", q.getId());
                    quizMap.put("title", q.getTitle());
                    quizMap.put("description", q.getDescription());
                    quizMap.put("totalMarks", q.getTotalMarks());
                    quizMap.put("passingScore", q.getPassingScore());
                    quizMap.put("durationInMinutes", q.getDurationInMinutes());
                    return quizMap;
                })
                .toList();

        return ResponseEntity.ok(quizzes);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get quiz by ID")
    public ResponseEntity<Map<String, Object>> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", quiz.getId());
        response.put("title", quiz.getTitle());
        response.put("questionsCount", quiz.getQuestions().size());

        return ResponseEntity.ok(response);
    }



    // Create new quiz
    @PostMapping
    @Operation(summary = "Create new quiz", description = "Create a new quiz (Admin only)")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizRepository.save(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    //Add question to quiz
    @PostMapping("/{quizId}/questions")
    @Operation(summary = "Add question to quiz", description = "Add a new question to a quiz")
    public ResponseEntity<Question> addQuestionToQuiz(
            @PathVariable Long quizId,
            @RequestBody Question question) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        question.setQuiz(quiz);
        Question savedQuestion = questionRepository.save(question);

        return ResponseEntity.ok(savedQuestion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quiz", description = "Delete a quiz by ID")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id) {
        quizRepository.deleteById(id);
        return ResponseEntity.ok("Quiz deleted successfully");
    }

}

