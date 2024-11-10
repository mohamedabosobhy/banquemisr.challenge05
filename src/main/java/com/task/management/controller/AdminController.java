package com.task.management.controller;

import com.task.management.auth.AuthAnnotation;
import com.task.management.exception.TaskManagementException;
import com.task.management.model.TaskResponse;
import com.task.management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private TaskService taskService;

    @DeleteMapping("/task/{id}")
    @AuthAnnotation
    public TaskResponse deleteTask(@PathVariable Long id) throws TaskManagementException {
        return taskService.deleteTask(id);
    }
}
