package com.task.management.controller;

import com.task.management.auth.AuthJwtToken;
import com.task.management.controller.AuthController;
import com.task.management.entity.User;
import com.task.management.exception.TaskManagementEnum;
import com.task.management.exception.TaskManagementException;
import com.task.management.model.AuthRequest;
import com.task.management.model.AuthResponse;
import com.task.management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthJwtToken jwtTokenUtil;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenUtil.generateToken(anyString(), any())).thenReturn("token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType("application/json")
                        .content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginFailure() throws Exception {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType("application/json")
                        .content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isUnauthorized());
    }
}
