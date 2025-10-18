package com.example.quiz_management.controller;

import com.example.quiz_management.dto.UserDto;
import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.QuizAttempt;
import com.example.quiz_management.entity.Role;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.service.QuizAttemptService;
import com.example.quiz_management.service.QuizService;
import com.example.quiz_management.service.UserLoginService;
import com.example.quiz_management.service.impl.UserLogInServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UserLogInServiceImplementation userLoginService;

    @Mock
    private QuizService quizService;

    @Mock
    private QuizAttemptService quizAttemptService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listParticipants_shouldReturnParticipantsListView() {
        // Arrange
        UserDto user = new UserDto();
        user.setId(1L);
        user.setFullName("Test User");
        Role role = new Role();
        role.setName("ROLE_PARTICIPANT");

        user.setRoles(new ArrayList<>(List.of(role)));

        when(userLoginService.findAllUsers()).thenReturn(List.of(user));

        User participant = new User();
        participant.setId(1L);
        Quiz quiz = new Quiz();
        quiz.setTitle("Sample Quiz");

        QuizAttempt attempt = new QuizAttempt();
        attempt.setParticipant(participant);
        attempt.setQuiz(quiz);

        when(quizAttemptService.findAll()).thenReturn(List.of(attempt));

        // Act
        String viewName = adminController.listParticipants(model);

        // Assert
        assertEquals("participants-list", viewName);
        verify(model).addAttribute(eq("participants"), any());
        verify(model).addAttribute(eq("userQuizMap"), any());
    }

    @Test
    void viewAllMarks_shouldReturnMarksListView_andModelHasAttempts() {

        QuizAttempt a1 = new QuizAttempt();
        a1.setId(5L);
        when(quizAttemptService.findAll()).thenReturn(Arrays.asList(a1));

        String view = adminController.viewAllMarks(model);

        assertEquals("marks-list", view);
        verify(model).addAttribute("attempts", Arrays.asList(a1));
    }

    @Test
    void adminDashboard_shouldReturnAdminView() {
        when(quizService.count()).thenReturn(5L);
        when(userLoginService.countByRoleName("ROLE_PARTICIPANT")).thenReturn(10L);
        when(quizAttemptService.count()).thenReturn(20L);
        when(quizAttemptService.countByStatus("SUBMITTED")).thenReturn(15L);

        QuizAttempt recentAttempt = new QuizAttempt();
        when(quizAttemptService.findTop5ByOrderByStartedAtDesc()).thenReturn(List.of(recentAttempt));

        String viewName = adminController.adminDashboard(model);

        assertEquals("admin", viewName);
        verify(model).addAttribute("totalQuizzes", 5L);
        verify(model).addAttribute("totalParticipants", 10L);
        verify(model).addAttribute("totalAttempts", 20L);
        verify(model).addAttribute("submittedAttempts", 15L);
        verify(model).addAttribute("recentAttempts", List.of(recentAttempt));
    }
}
