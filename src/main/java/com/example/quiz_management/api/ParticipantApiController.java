package com.example.quiz_management.api;

import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.service.QuizAttemptService;
import com.example.quiz_management.service.QuizService;
import com.example.quiz_management.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
@Tag(name = "Participant API", description = "API for quiz participants to view and take quizzes")
public class ParticipantApiController {

    private final QuizService quizService;
    private final QuizAttemptService quizAttemptService;
    private final UserLoginService userLoginService;

    // Get all quizzes
    @GetMapping("/quizzes")
    @Operation(summary = "List all available quizzes")
    public ResponseEntity<List<Map<String, Object>>> getAllQuizzes() {
        List<Map<String, Object>> quizzes = quizService.findAll().stream()
                .map(q -> {
                    Map<String, Object> quizMap = new HashMap<>();
                    quizMap.put("id", q.getId());
                    quizMap.put("title", q.getTitle());
                    quizMap.put("description", q.getDescription());
                    quizMap.put("durationInMinutes", q.getDurationInMinutes());
                    quizMap.put("totalMarks", q.getTotalMarks());
                    return quizMap;
                })
                .toList();
        return ResponseEntity.ok(quizzes);
    }

    //  Start or resume quiz attempt
    @PostMapping("/quizzes/{quizId}/attempt")
    @Operation(summary = "Start or resume a quiz attempt")
    public ResponseEntity<Map<String, Object>> attemptQuiz(
            @PathVariable Long quizId,
            @RequestParam String email // instead of authentication
    ) {
        User participant = userLoginService.findByEmail(email);
        QuizAttempt attempt = quizAttemptService.startOrResumeAttempt(quizId, participant);

        Map<String, Object> response = new HashMap<>();
        response.put("quizId", attempt.getQuiz().getId());
        response.put("title", attempt.getQuiz().getTitle());
        response.put("durationSeconds", attempt.getQuiz().getDurationInMinutes() * 60);
        response.put("attemptId", attempt.getId());
        return ResponseEntity.ok(response);
    }

    //  Submit answers
    @PostMapping("/quizzes/{quizId}/submit")
    @Operation(summary = "Submit quiz answers and calculate score")
    public ResponseEntity<Map<String, Object>> submitQuiz(
            @PathVariable Long quizId,
            @RequestParam String email,
            @RequestBody Map<String, String> answers
    ) {
        User participant = userLoginService.findByEmail(email);
        QuizAttempt attempt = quizAttemptService.submitAttempt(quizId, participant, answers);

        Map<String, Object> result = new HashMap<>();
        result.put("quizTitle", attempt.getQuiz().getTitle());
        result.put("totalScore", attempt.getTotalScore());
        result.put("status", "COMPLETED");
        return ResponseEntity.ok(result);
    }

    //  View participant’s quiz marks
    @GetMapping("/marks")
    @Operation(summary = "View all quiz attempts and scores by participant")
    public ResponseEntity<List<Map<String, Object>>> getParticipantMarks(
            @RequestParam String email
    ) {
        User participant = userLoginService.findByEmail(email);
        List<Map<String, Object>> attempts = quizAttemptService.findAllByParticipant(participant).stream()
                .map(a -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("quizTitle", a.getQuiz().getTitle());
                    map.put("totalScore", a.getTotalScore());
                    map.put("completedAt", a.getSubmittedAt());
                    return map;
                })
                .toList();

        return ResponseEntity.ok(attempts);
    }
}

