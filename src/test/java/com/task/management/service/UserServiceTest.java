package com.task.management.service;

import com.task.management.entity.User;
import com.task.management.model.UserRequest;
import com.task.management.model.UserRespone;
import com.task.management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("password");
        userRequest.setEmail("test@example.com");
        userRequest.setRole("USER");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        String result = userService.register(userRequest);

        verify(userRepository).save(any(User.class));
        assertEquals("User registered successfully!", result);
    }

    @Test
    public void testGetUsers() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole("USER");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserRespone> users = userService.getUsers();

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }
}
