package com.example.quiz_management.service;

import com.example.quiz_management.dto.UserDto;
import com.example.quiz_management.entity.Role;
import com.example.quiz_management.entity.User;
import com.example.quiz_management.repository.UserRepository;
import com.example.quiz_management.service.impl.UserLogInServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserLoginServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLogInServiceImplementation userLoginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllUsers_shouldReturnListOfUserDto() {
        // 🔹 Mock user entity
        User user = new User();
        user.setId(1L);
        user.setFullName("Alice");
        Role role = new Role();
        role.setName("ROLE_PARTICIPANT");


        // 🔹 Mock repository call
        when(userRepository.findAll()).thenReturn(List.of(user));

        // 🔹 Mock ModelMapper behavior
        UserDto dto = new UserDto();
        dto.setFullName("Alice");
        dto.setEmail("alice@example.com");
        dto.setRoles(List.of(role));
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(dto);

        // 🔹 Call service
        List<UserDto> users = userLoginService.findAllUsers();

        // 🔹 Assertions
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Alice", users.get(0).getFullName());
        assertEquals("alice@example.com", users.get(0).getEmail());

        // 🔹 Verify interaction
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void countByRoleName_shouldReturnCount() {
        when(userRepository.countByRoleName("ROLE_PARTICIPANT")).thenReturn(3L);

        long count = userLoginService.countByRoleName("ROLE_PARTICIPANT");

        assertEquals(3L, count);
        verify(userRepository, times(1)).countByRoleName("ROLE_PARTICIPANT");
    }
}
