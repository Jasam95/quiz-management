package com.example.quiz_management.repository;

import com.example.quiz_management.QuizManagementApplication;
import com.example.quiz_management.entity.Choice;
import com.example.quiz_management.entity.Question;
import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = QuizManagementApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById_shouldWork() {
        // ✅ Step 1: Persist admin properly
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setPassword("secure123");
        admin = userRepository.saveAndFlush(admin); // Ensure it’s persisted and managed

        // ✅ Step 2: Create and persist quiz linked to admin
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Basics Quiz");
        quiz.setDescription("Quiz on Java fundamentals");
        quiz.setAdmin(admin);
        quiz.setTotalMarks(100);
        quiz.setDurationInMinutes(30);
        quiz.setPassingScore(50.0);
        quiz.setMaxAttempts(1);
        quiz.setCreatedAt(LocalDateTime.now());

        Quiz savedQuiz = quizRepository.saveAndFlush(quiz);

        // ✅ Step 3: Create question with valid quiz
        Question question = new Question();
        question.setQuestionText("What is JVM?");
        question.setPoints(1.0);
        question.setQuiz(savedQuiz);

        // ✅ Step 4: Add choices
        Choice choice1 = new Choice();
        choice1.setText("Java Virtual Machine");
        choice1.setIsCorrect(true);
        choice1.setQuestion(question);

        Choice choice2 = new Choice();
        choice2.setText("Java Version Manager");
        choice2.setIsCorrect(false);
        choice2.setQuestion(question);

        question.setChoices(List.of(choice1, choice2));

        Question savedQuestion = questionRepository.saveAndFlush(question);

        // ✅ Step 5: Fetch and verify
        Optional<Question> fetched = questionRepository.findById(savedQuestion.getId());

        assertThat(fetched).isPresent();
        assertThat(fetched.get().getQuestionText()).isEqualTo("What is JVM?");
        assertThat(fetched.get().getQuiz().getTitle()).isEqualTo("Java Basics Quiz");
        assertThat(fetched.get().getChoices()).hasSize(2);
        assertThat(fetched.get().getChoices().get(0).getQuestion()).isNotNull();
    }
}
