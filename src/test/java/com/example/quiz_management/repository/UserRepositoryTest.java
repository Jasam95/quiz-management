package com.example.quiz_management.repository;

import com.example.quiz_management.QuizManagementApplication;
import com.example.quiz_management.entity.Role;
import com.example.quiz_management.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = QuizManagementApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
@Transactional
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    private Role findOrCreateRole(String name) {
        Role role = new Role(null, name);
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(role));
    }

    @Test
    void saveAndFindById_shouldWork() {
        Role role = findOrCreateRole("ROLE_PARTICIPANT");

        User user = new User();
        user.setFullName("Deepa");
        user.setEmail("deepa@example.com");
        user.setPassword("pass");
        user.setRoles(new ArrayList<>(List.of(role)));

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());
    }

    @Test
    void countByRolesName_shouldReturnCorrectCount() {
        Role role = findOrCreateRole("ROLE_PARTICIPANT");

        User user1 = new User();
        user1.setFullName("Alice");
        user1.setEmail("alice@example.com");
        user1.setPassword("pass");
        user1.setRoles(new ArrayList<>(List.of(role)));

        userRepository.save(user1);

        long count = userRepository.countByRoleName("ROLE_PARTICIPANT");
        assertEquals(1, count);
    }
}

