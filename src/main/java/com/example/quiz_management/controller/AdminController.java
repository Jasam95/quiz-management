package com.example.quiz_management.controller;

import com.example.quiz_management.dto.UserDto;
import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.repository.QuizAttemptRepository;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.service.QuizAttemptService;
import com.example.quiz_management.service.QuizService;
import com.example.quiz_management.service.UserLoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class AdminController {

    private UserLoginService userLoginService;

    private QuizService quizService;

    private QuizAttemptService quizAttemptService;

    @GetMapping("/admin/participants")
    public String listParticipants(Model model) {
        List<UserDto> participants = userLoginService.findAllUsers()
                .stream()
                .filter(u -> u.getRoles().stream()
                        .anyMatch(r -> r.getName().equals("ROLE_PARTICIPANT")))
                .collect(Collectors.toList());

        // Fetch all attempts
        List<QuizAttempt> attempts = quizAttemptService.findAll();

        Map<Long, List<String>> userQuizMap = attempts.stream()
                .filter(a -> a.getQuiz() != null && a.getParticipant() != null)
                .collect(Collectors.groupingBy(
                        a -> a.getParticipant().getId(),
                        Collectors.collectingAndThen(
                                Collectors.mapping(a -> a.getQuiz().getTitle(), Collectors.toSet()),
                                set -> new ArrayList<>(set)
                        )
                ));


        model.addAttribute("participants", participants);
        model.addAttribute("userQuizMap", userQuizMap);
        return "participants-list";
    }

    @GetMapping("/admin/marks")
    public String viewAllMarks(Model model) {
        List<QuizAttempt> attempts = quizAttemptService.findAll(); // or filter by quiz
        model.addAttribute("attempts", attempts);
        return "marks-list"; // Thymeleaf template
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        long totalQuizzes = quizService.count();
        long totalParticipants = userLoginService.countByRoleName("ROLE_PARTICIPANT");
        long totalAttempts = quizAttemptService.count();
        long submittedAttempts = quizAttemptService.countByStatus("SUBMITTED");

        List<QuizAttempt> recentAttempts = quizAttemptService.findTop5ByOrderByStartedAtDesc();

        model.addAttribute("totalQuizzes", totalQuizzes);
        model.addAttribute("totalParticipants", totalParticipants);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("submittedAttempts", submittedAttempts);
        model.addAttribute("recentAttempts", recentAttempts);

        return "admin";
    }

}
