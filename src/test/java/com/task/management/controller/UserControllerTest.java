package com.task.management.controller;

import com.task.management.auth.AuthJwtToken;
import com.task.management.entity.Task;
import com.task.management.model.*;
import com.task.management.service.TaskService;
import com.task.management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TaskService taskService;
    Map<String, Object> clams= new HashMap<>();
    AuthJwtToken authJwtToken= new AuthJwtToken();
    String token;

    @BeforeEach
    public void step(){
        clams.put("role","user");
        token = authJwtToken.generateToken("user",clams);
    }

    @Test
    public void testRegister() throws Exception {
        when(userService.register(any(UserRequest.class))).thenReturn("User registered successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .contentType("application/json")
                        .content("{\"username\":\"user\",\"password\":\"pass\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/allUsers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateTask() throws Exception {
        TaskResponse taskResponse = new TaskResponse();
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/task")
                        .contentType("application/json")
                        .content("{\"title\":\"New Task\",\"description\":\"Task description\"}")
                        .header("Authorization", "Bearer "+token)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTask() throws Exception {
        TaskResponse taskResponse = new TaskResponse();
        when(taskService.updateTask(any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/task")
                        .header("Authorization", "Bearer "+token)
                        .contentType("application/json")
                        .content("{\"id\":1,\"title\":\"Updated Task\",\"description\":\"Updated description\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testInbox() throws Exception {
        when(taskService.getTasks(
                anyInt(), // size
                anyInt(), // page
                anyString(), // title
                any(Task.Status.class), // status
                any(Task.Priority.class), // priority
                anyString(), // description
                any(LocalDateTime.class), // dueDateFrom
                any(LocalDateTime.class), // dueDateTo
                any(LocalDateTime.class), // createDateFrom
                any(LocalDateTime.class), // createDateTo
                anyLong() // userId
        )).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/task"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHistory() throws Exception {
        when(taskService.geth()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/history"))
                .andExpect(status().isOk());
    }
}
