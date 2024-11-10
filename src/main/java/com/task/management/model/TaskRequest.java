package com.task.management.model;

import com.task.management.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest  {
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
    private String user_id;
    private Long id;

}
