package com.task.management.service;

import com.task.management.auth.CustomUserDetails;
import com.task.management.entity.History;
import com.task.management.entity.Task;
import com.task.management.entity.User;
import com.task.management.exception.TaskManagementException;
import com.task.management.model.TaskRequest;
import com.task.management.model.TaskResponse;
import com.task.management.repository.HistoryRepository;
import com.task.management.repository.TaskRepository;
import com.task.management.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    EmailService emailService;

    @InjectMocks
    private TaskService taskService;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(customUserDetails.getUserId()).thenReturn("1");
        when(historyRepository.save(any())).thenReturn(new History());

    }
    @AfterEach
    public void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    public void testCreateTask() throws TaskManagementException {
        User user = User.builder().id(1L).email("test@example.com").username("testusername").build();
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        TaskRequest taskRequest = TaskRequest.builder().title("New Task")
                .description("Task description")
                .user_id("1")
                .priority(Task.Priority.LOW.toString())
                .status(Task.Status.IN_PROGRESS.toString()).build();
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Task description");
        task.setUser(user);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        assertEquals("New Task", taskResponse.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() throws TaskManagementException {
        User user = User.builder().id(1L).email("test@example.com").username("testusername").build();
        TaskRequest taskRequest = TaskRequest.builder().title("Updated Task").id(1L)
                .description("Task description")
                .user_id("1")
                .priority(Task.Priority.LOW.toString())
                .status(Task.Status.IN_PROGRESS.toString()).build();
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task");
        task.setDescription("Updated description");
        task.setUser(user);
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskResponse taskResponse = taskService.updateTask(taskRequest);
        assertEquals("Updated Task", taskResponse.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void testDeleteTask() throws TaskManagementException {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse taskResponse = taskService.deleteTask(1L);

        assertEquals(1L, taskResponse.getId());

    }

    @Test
    public void testDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskManagementException.class, () -> {
            taskService.deleteTask(1L);
        });
    }
}

