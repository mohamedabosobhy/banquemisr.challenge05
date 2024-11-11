package com.task.management.controller;

import com.task.management.auth.AuthJwtToken;
import com.task.management.exception.TaskManagementEnum;
import com.task.management.exception.TaskManagementException;
import com.task.management.model.TaskResponse;
import com.task.management.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    @Test
    public void testDeleteTaskSuccess() throws Exception {
        TaskResponse taskResponse = new TaskResponse();
        when(taskService.deleteTask(anyLong())).thenReturn(taskResponse);
        AuthJwtToken authJwtToken= new AuthJwtToken();
        Map clams= new HashMap<>();
        clams.put("role","admin");
        String token = authJwtToken.generateToken("user",clams);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/task/1")
                        .header("Authorization", "Bearer "+token))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTaskFailure() throws Exception {
        Map clams= new HashMap<>();
        clams.put("role","admin");
        AuthJwtToken authJwtToken= new AuthJwtToken();
        String token = authJwtToken.generateToken("user",clams);
        when(taskService.deleteTask(anyLong())).thenThrow(TaskManagementEnum.TASK_MANAGEMENT_EXCEPTION_ENUM_07.getValue());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/task/1")
                        .header("Authorization", "Bearer "+token))

                .andExpect(status().isNotFound());
    }
}

