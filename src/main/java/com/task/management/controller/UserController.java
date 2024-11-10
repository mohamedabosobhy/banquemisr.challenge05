package com.task.management.controller;

import com.task.management.auth.AuthAnnotation;
import com.task.management.entity.History;
import com.task.management.entity.Task;
import com.task.management.exception.TaskManagementException;
import com.task.management.model.*;
import com.task.management.service.TaskService;
import com.task.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;



    @PostMapping("/register")
    public String register(@RequestBody UserRequest user) {
        return userService.register(user);
    }
    @GetMapping("/allUsers")
    //@AuthAnnotation
    public List<UserRespone> getAllUsers() {
        return userService.getUsers();
    }
    @PostMapping("/task")
    @AuthAnnotation
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) throws TaskManagementException {
    return taskService.createTask(taskRequest);
    }
    @PutMapping("/task")
    @AuthAnnotation
    public TaskResponse updateTask(@RequestBody TaskRequest taskRequest) throws TaskManagementException {
        return taskService.updateTask(taskRequest);
    }

    @GetMapping("/task")
    public List<TaskResponse> inbox( @RequestParam(required = false) String title,
                                     @RequestParam(required = false) Task.Status status,
                                     @RequestParam(required = false) Task.Priority priority,
                                     @RequestParam(required = false) Long userId,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime dueDateFrom,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime dueDateTo,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime createDateFrom,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime createDateTo,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) throws TaskManagementException {
        return taskService.getTasks(size,page,title,status,priority,description,dueDateFrom,dueDateTo,createDateFrom,createDateTo,userId);
    }

    @GetMapping("/h")
    //@AuthAnnotation
    public List<HistoryResponse> geth() {
        return taskService.geth();
    }
}
