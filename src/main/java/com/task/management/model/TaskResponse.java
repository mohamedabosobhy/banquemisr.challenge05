package com.task.management.model;

import com.task.management.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private Task.Priority priority;
    private String assignUser;
    private String createdUser;
    private LocalDateTime dueDate;
    private LocalDateTime createdDate;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", assignUser='" + assignUser + '\'' +
                ", createdUser='" + createdUser + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }
}
