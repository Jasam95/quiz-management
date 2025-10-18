package com.example.quiz_management.repository;


import com.example.quiz_management.QuizManagementApplication;
import com.example.quiz_management.entity.Quiz;
import com.example.quiz_management.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = QuizManagementApplication.class)
@EntityScan(basePackages = "com.example.quiz_management.entity")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class QuizRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkDataSource() throws Exception {
        System.out.println("DB URL: " + dataSource.getConnection().getMetaData().getURL());
    }



    @Test
    void verifyTablesCreated() throws Exception {
        var conn = dataSource.getConnection();
        var rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
        System.out.println("🧩 Tables created:");
        while (rs.next()) {
            System.out.println(" - " + rs.getString("TABLE_NAME"));
        }
        conn.close();
    }



    @Test
    void saveAndFindById_shouldWork() {
        // Create an admin user
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setPassword("secure123");
        userRepository.save(admin);

        // Create quiz with all required fields
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Basics");
        quiz.setDescription("A basic quiz on Java fundamentals");
        quiz.setTotalMarks(100);
        quiz.setDurationInMinutes(30);
        quiz.setPassingScore(50.0);
        quiz.setMaxAttempts(1);
        quiz.setAdmin(admin); // 🔹 Important: link admin here

        Quiz saved = quizRepository.save(quiz);

        Optional<Quiz> fetched = quizRepository.findById(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Java Basics", fetched.get().getTitle());
        assertEquals("Admin User", fetched.get().getAdmin().getFullName());
    }



    @Test
    void count_shouldReturnNumberOfQuizzes() {
        Quiz q1 = new Quiz();
        q1.setTitle("Q1");
        q1.setDescription("Quiz about Java");
        q1.setTotalMarks(100);
        q1.setDurationInMinutes(30);
        q1.setPassingScore(50.0);
        q1.setMaxAttempts(1);

        Quiz q2 = new Quiz();
        q2.setTitle("Q2");
        q2.setDescription("Quiz about Spring Boot");
        q2.setTotalMarks(100);
        q2.setDurationInMinutes(30);
        q2.setPassingScore(50.0);
        q2.setMaxAttempts(1);

        quizRepository.save(q1);
        quizRepository.save(q2);

        assertEquals(2, quizRepository.count());
    }

}
