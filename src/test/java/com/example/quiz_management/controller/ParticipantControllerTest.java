package com.example.quiz_management.controller;

import com.example.quiz_management.entity.*;
import com.example.quiz_management.repository.QuizAttemptRepository;
import com.example.quiz_management.repository.QuizRepository;
import com.example.quiz_management.service.QuizAttemptService;
import com.example.quiz_management.service.QuizService;
import com.example.quiz_management.service.UserLoginService;
import com.example.quiz_management.service.impl.UserLogInServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ParticipantControllerTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizService quizService;

    @Mock
    private UserLogInServiceImplementation userLoginService;

    @Mock
    private QuizAttemptService quizAttemptService;

    @Mock
    private QuizAttemptRepository quizAttemptRepository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private ParticipantController participantController;

    private User participant;
    private Quiz quiz;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        participant = new User();
        participant.setId(1L);
        participant.setFullName("Alice");
        participant.setEmail("alice@example.com");
        participant.setPassword("12345");

        quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java Basics");
        quiz.setDescription("Simple Java test");
        quiz.setTotalMarks(100);
        quiz.setPassingScore(50.0);
        quiz.setDurationInMinutes(30);
        quiz.setMaxAttempts(1);
        quiz.setAdmin(participant);
        quiz.setCreatedAt(LocalDateTime.now());
    }

    // Test 1: Display all quizzes


    @Test
    void showAllQuizzes_shouldAddQuizzesToModelAndReturnView() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java Basics");

        when(quizService.findAll()).thenReturn(List.of(quiz));

        String view = participantController.showAllQuizzes(model);

        verify(model).addAttribute("quizzes", List.of(quiz));
        assertThat(view).isEqualTo("participant/quizzes");
    }

    //  Test 2: Attempt quiz (new attempt)
    @Test
    void attemptQuiz_shouldCreateNewAttemptWhenNoneExists() {
        // Arrange
        when(userDetails.getUsername()).thenReturn("alice@example.com");
        when(userLoginService.findByEmail("alice@example.com")).thenReturn(participant);

        quiz.setId(1L);
        quiz.setTitle("Java Basics");
        quiz.setDurationInMinutes(30);

        QuizAttempt attempt = new QuizAttempt();
        attempt.setId(1L);
        attempt.setQuiz(quiz);
        attempt.setParticipant(participant);

        when(quizAttemptService.startOrResumeAttempt(1L, participant)).thenReturn(attempt);

        // Act
        String view = participantController.attemptQuiz(1L, model, userDetails);

        // Assert
        verify(model).addAttribute("quiz", quiz);
        verify(model).addAttribute("attempt", attempt);
        verify(model).addAttribute("durationSeconds", 30 * 60);
        assertThat(view).isEqualTo("participant/quiz_attempt");
    }

    @Test
    void submitQuiz_shouldSaveAttemptAndRedirectToResult() {
        // Mock logged-in user
        when(userDetails.getUsername()).thenReturn("alice@example.com");
        when(userLoginService.findByEmail("alice@example.com")).thenReturn(participant);

        // Create quiz and mock data
        Choice choice = new Choice();
        choice.setId(1L);
        choice.setText("Java Virtual Machine");
        choice.setIsCorrect(true);

        Question question = new Question();
        question.setId(1L);
        question.setQuestionText("What is JVM?");
        question.setPoints(1.0);
        question.setChoices(List.of(choice));

        quiz.setQuestions(List.of(question));

        QuizAttempt attempt = new QuizAttempt();
        attempt.setId(1L);
        attempt.setQuiz(quiz);
        attempt.setParticipant(participant);
        attempt.setTotalScore(1.0);
        attempt.setStatus("COMPLETED");

        // ✅ Mock the submitAttempt call (this is what the controller calls)
        when(quizAttemptService.submitAttempt(eq(1L), eq(participant), anyMap()))
                .thenReturn(attempt);

        // Mock parameters for form submission
        Map<String, String> params = Map.of("selectedChoiceId_1", "1");

        // Execute
        String viewName = participantController.submitQuiz(1L, params, userDetails, redirectAttributes);

        // ✅ Verify the correct behavior
        verify(userLoginService).findByEmail("alice@example.com");
        verify(quizAttemptService).submitAttempt(eq(1L), eq(participant), anyMap());
        verify(redirectAttributes).addFlashAttribute("score", 1.0);
        verify(redirectAttributes).addFlashAttribute("quiz", quiz);
        verify(redirectAttributes).addFlashAttribute("attempt", attempt);

        // ✅ Check redirect
        assertThat(viewName).isEqualTo("redirect:/participant/quiz_result");
    }

    @Test
    void showQuizResult_shouldReturnResultView() {
        String viewName = participantController.showQuizResult();
        assertThat(viewName).isEqualTo("quiz_result");
    }

    //  Test 5: View marks page
    @Test
    void myScores_shouldAddAttemptsToModelAndReturnMarksView() {
        when(userDetails.getUsername()).thenReturn("alice@example.com");
        when(userLoginService.findByEmail("alice@example.com")).thenReturn(participant);

        List<QuizAttempt> attempts = List.of(new QuizAttempt());
        when(quizAttemptService.findAllByParticipant(participant)).thenReturn(attempts);

        String viewName = participantController.myScores(userDetails, model);

        verify(model).addAttribute("attempts", attempts);
        assertThat(viewName).isEqualTo("marks");
    }
}
