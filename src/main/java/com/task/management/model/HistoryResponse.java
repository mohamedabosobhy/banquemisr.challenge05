package com.task.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private Long id;
    private String userUpdated;
    private Long taskId;
    private String description;
    private LocalDateTime date;
    private String action;

}
