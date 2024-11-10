package com.task.management.model;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;

}