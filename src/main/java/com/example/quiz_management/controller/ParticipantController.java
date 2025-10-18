package com.example.quiz_management.controller;

import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.service.QuizAttemptService;
import com.example.quiz_management.service.QuizService;
import com.example.quiz_management.service.UserLoginService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class ParticipantController {

    private final QuizService quizService;
    private final QuizAttemptService quizAttemptService;
    private final UserLoginService userLoginService;

    //  Display all quizzes
    @GetMapping("/participants/quizzes")
    public String showAllQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.findAll());
        return "participant/quizzes";
    }

    // Start or resume quiz
    @GetMapping("/participants/quizzes/{quizId}/attempt")
    public String attemptQuiz(
            @PathVariable Long quizId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User participant = userLoginService.findByEmail(userDetails.getUsername());
        QuizAttempt attempt = quizAttemptService.startOrResumeAttempt(quizId, participant);

        model.addAttribute("quiz", attempt.getQuiz());
        model.addAttribute("attempt", attempt);
        model.addAttribute("durationSeconds", attempt.getQuiz().getDurationInMinutes() * 60);

        return "participant/quiz_attempt";
    }

    //  Submit quiz answers
    @PostMapping("/participants/quizzes/{quizId}/attempt")
    public String submitQuiz(
            @PathVariable Long quizId,
            @RequestParam Map<String, String> allParams,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        User participant = userLoginService.findByEmail(userDetails.getUsername());
        QuizAttempt attempt = quizAttemptService.submitAttempt(quizId, participant, allParams);

        redirectAttributes.addFlashAttribute("score", attempt.getTotalScore());
        redirectAttributes.addFlashAttribute("quiz", attempt.getQuiz());
        redirectAttributes.addFlashAttribute("attempt", attempt);

        return "redirect:/participant/quiz_result";
    }

    // Show quiz result
    @GetMapping("/participant/quiz_result")
    public String showQuizResult() {
        return "quiz_result";
    }

    //  Show participant’s quiz marks
    @GetMapping("/participants/marks")
    public String myScores(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User participant = userLoginService.findByEmail(userDetails.getUsername());
        model.addAttribute("attempts", quizAttemptService.findAllByParticipant(participant));
        return "marks";
    }
}
