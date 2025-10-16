package com.example.quiz_management.controller;

import com.example.quiz_management.entity.*;
import com.example.quiz_management.repository.QuizAttemptRepository;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.service.QuizAttemptService;
import com.example.quiz_management.service.QuizService;
import com.example.quiz_management.service.UserLoginService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class participantController {

    private final QuizRepository quizRepository;

    private QuizService quizService;

    private UserLoginService userLoginService;

    private QuizAttemptService quizAttemptService;

    private QuizAttemptRepository quizAttemptRepository;

        // Page to display all quizzes
        @GetMapping("/participants/quizzes")
        public String showAllQuizzes(Model model) {
            List<Quiz> quizzes = quizRepository.findAll();
            model.addAttribute("quizzes", quizzes);
            return "participant/quizzes"; // Thymeleaf page
        }

    @GetMapping("/participants/quizzes/{quizId}/attempt")
    public String attemptQuiz(@PathVariable Long quizId,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        User participant = userLoginService.findByEmail(userDetails.getUsername());

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // Check if participant has an existing IN_PROGRESS attempt
        Optional<QuizAttempt> existingAttempt = quizAttemptRepository
                .findByQuizAndParticipantAndStatus(quiz, participant, "IN_PROGRESS");

        QuizAttempt attempt;
        if (existingAttempt.isPresent()) {
            attempt = existingAttempt.get(); // resume existing attempt
        } else {
            attempt = QuizAttempt.builder()
                    .quiz(quiz)
                    .participant(participant)
                    .status("IN_PROGRESS")
                    .startedAt(LocalDateTime.now())
                    .build();
            quizAttemptRepository.save(attempt);
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("attempt", attempt);
        model.addAttribute("durationSeconds", quiz.getDurationInMinutes() * 60);

        return "participant/quiz_attempt";
    }


    @PostMapping("/participants/quizzes/{quizId}/attempt")
    public String submitQuiz(
            @PathVariable Long quizId,
            @RequestParam Map<String, String> allParams,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        // 1️⃣ Get participant
        User participant = userLoginService.findByEmail(userDetails.getUsername());

        // 2️⃣ Load quiz
        Quiz quiz = quizService.getQuizById(quizId);

        // 3️⃣ Load the existing attempt for this participant & quiz
        QuizAttempt attempt = quizAttemptRepository
                .findByQuizAndParticipantAndStatus(quiz, participant, "IN_PROGRESS")
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (attempt.getUserAnswers() == null) {
            attempt.setUserAnswers(new ArrayList<>());
        } else {
            attempt.getUserAnswers().clear();
        }
        double totalScore = 0.0;

        // 4️⃣ Loop through all questions
        for (Question question : quiz.getQuestions()) {
            String paramName = "selectedChoiceId_" + question.getId();
            String selectedChoiceIdStr = allParams.get(paramName);

            UserAnswer answer = new UserAnswer();
            answer.setQuizAttempt(attempt);
            answer.setQuestion(question);

            if (selectedChoiceIdStr != null && !selectedChoiceIdStr.isEmpty()) {
                Long selectedChoiceId = Long.valueOf(selectedChoiceIdStr);

                //Get Choice from DB and check with User Choice
                Choice selectedChoice = question.getChoices()
                        .stream()
                        .filter(c -> c.getId().equals(selectedChoiceId))
                        .findFirst()
                        .orElse(null);

                answer.setSelectedChoiceIds(String.valueOf(selectedChoiceId));

                if (selectedChoice != null && selectedChoice.getIsCorrect()) {
                    answer.setIsCorrect(true);
                    totalScore += question.getPoints();
                    answer.setPointsForEachAnswer(question.getPoints());
                } else {
                    answer.setIsCorrect(false);
                }
            } else {
                // User skipped
                answer.setSelectedChoiceIds(null);
                answer.setIsCorrect(false);
            }

            attempt.getUserAnswers().add(answer);
        }

        // 5️⃣ Finalize attempt
        attempt.setTotalScore(totalScore);
        attempt.setStatus("SUBMITTED");
        attempt.setSubmittedAt(LocalDateTime.now());

        // 6️⃣ Calculate time taken
        int seconds = (int) java.time.Duration
                .between(attempt.getStartedAt(), attempt.getSubmittedAt())
                .getSeconds();
        attempt.setTimeTakenSeconds(seconds);

        // 7️⃣ Save attempt
        quizAttemptRepository.save(attempt);

        redirectAttributes.addFlashAttribute("score", totalScore);
        redirectAttributes.addFlashAttribute("quiz", quiz);
        redirectAttributes.addFlashAttribute("attempt", attempt);

        return  "redirect:/participant/quiz_result";
    }

    @GetMapping("/participant/quiz_result")
    public String showQuizResult(Model model) {
        // The score or quizId will be available in the model via FlashAttributes
        return "quiz_result"; // Thymeleaf template
    }

    @GetMapping("/participants/marks")
    public String myScores(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User participant = userLoginService.findByEmail(userDetails.getUsername());
        List<QuizAttempt> attempts = quizAttemptService.findAllByParticipant(participant);

        model.addAttribute("attempts", attempts);
        return "marks"; // Thymeleaf page
    }



}