package com.task.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespone {
    private String username;
    private String role;
    private Long id;
    private String email;
}
