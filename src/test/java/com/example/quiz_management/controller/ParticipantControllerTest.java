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
        List<Quiz> quizzes = List.of(quiz);
        when(quizRepository.findAll()).thenReturn(quizzes);

        String viewName = participantController.showAllQuizzes(model);

        verify(model).addAttribute("quizzes", quizzes);
        assertThat(viewName).isEqualTo("participant/quizzes");
    }

    //  Test 2: Attempt quiz (new attempt)
    @Test
    void attemptQuiz_shouldCreateNewAttemptWhenNoneExists() {
        when(userDetails.getUsername()).thenReturn("alice@example.com");
        when(userLoginService.findByEmail(anyString())).thenReturn(participant);
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(quizAttemptRepository.findByQuizAndParticipantAndStatus(any(), any(), eq("IN_PROGRESS")))
                .thenReturn(Optional.empty());

        String viewName = participantController.attemptQuiz(1L, model, userDetails);

        verify(quizAttemptRepository).save(any(QuizAttempt.class));
        verify(model).addAttribute(eq("quiz"), any(Quiz.class));
        verify(model).addAttribute(eq("attempt"), any(QuizAttempt.class));
        assertThat(viewName).isEqualTo("participant/quiz_attempt");
    }

    //  Test 3: Submit quiz
    @Test
    void submitQuiz_shouldSaveAttemptAndRedirectToResult() {
        // Mock user details
        when(userDetails.getUsername()).thenReturn("alice@example.com");
        when(userLoginService.findByEmail("alice@example.com")).thenReturn(participant);

        // Create a choice
        Choice choice = new Choice();
        choice.setId(1L);
        choice.setText("Java Virtual Machine");
        choice.setIsCorrect(true);

        // Create a question
        Question question = new Question();
        question.setId(1L);
        question.setQuestionText("What is JVM?");
        question.setPoints(1.0);
        question.setChoices(List.of(choice));

        // Create a quiz with that question
        quiz.setQuestions(List.of(question));

        // Mock service behavior
        when(quizService.getQuizById(1L)).thenReturn(quiz);

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setParticipant(participant);
        attempt.setStatus("IN_PROGRESS");
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setUserAnswers(new ArrayList<>());

        when(quizAttemptRepository.findByQuizAndParticipantAndStatus(quiz, participant, "IN_PROGRESS"))
                .thenReturn(Optional.of(attempt));

        Map<String, String> params = new HashMap<>();
        params.put("selectedChoiceId_1", "1");

        String result = participantController.submitQuiz(
                1L, params, userDetails, redirectAttributes);

        verify(quizAttemptRepository).save(any(QuizAttempt.class));
        verify(redirectAttributes).addFlashAttribute(eq("score"), anyDouble());
        assertThat(result).isEqualTo("redirect:/participant/quiz_result");
    }

    // Test 4: Show result page
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
