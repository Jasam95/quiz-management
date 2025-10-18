package com.example.quiz_management.repository;

import com.example.quiz_management.QuizManagementApplication;
import com.example.quiz_management.entity.QuizAttempt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = QuizManagementApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class QuizAttemptRepositoryTest {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Test
    void saveAndFindById_shouldWork() {
        QuizAttempt a = new QuizAttempt();
        // set necessary fields, e.g., status, participant, quiz etc.
        a.setStatus("SUBMITTED");
        QuizAttempt saved = quizAttemptRepository.save(a);

        assertNotNull(saved.getId());

        QuizAttempt fetched = quizAttemptRepository.findById(saved.getId()).orElse(null);
        assertNotNull(fetched);
        assertEquals("SUBMITTED", fetched.getStatus());
    }

    @Test
    void countByStatus_shouldReturnCorrectCount() {
        QuizAttempt a1 = new QuizAttempt();
        a1.setStatus("A");
        quizAttemptRepository.save(a1);
        QuizAttempt a2 = new QuizAttempt();
        a2.setStatus("B");
        quizAttemptRepository.save(a2);
        QuizAttempt a3 = new QuizAttempt();
        a3.setStatus("A");
        quizAttemptRepository.save(a3);

        long countA = quizAttemptRepository.countByStatus("A");
        assertEquals(2, countA);
    }
}
