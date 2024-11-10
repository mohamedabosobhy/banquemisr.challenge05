package com.task.management.controller;

import com.task.management.exception.TaskManagementException;
import com.task.management.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String errorMessage = "Invalid JSON format: " + ex.getLocalizedMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorMessage, "JSON parse error");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TaskManagementException.class)
    public ResponseEntity<?> handleGeneralException(TaskManagementException ex) {
        return new ResponseEntity<>(ex.getApiError(), ex.getApiError().getStatus());
    }
}