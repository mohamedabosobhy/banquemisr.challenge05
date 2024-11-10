package com.task.management.exception;

import com.task.management.entity.Task;
import com.task.management.model.ApiError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum TaskManagementEnum {
    TASK_MANAGEMENT_EXCEPTION_ENUM_01(TaskManagementException.builder().apiError(ApiError.builder().message("Filed 'priority' is mandatory ").status(HttpStatus.BAD_REQUEST).build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_02(TaskManagementException.builder().apiError(ApiError.builder().message("Filed 'priority' value is  " + Arrays.toString(Task.Priority.values())).status(HttpStatus.BAD_REQUEST).build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_03(TaskManagementException.builder().apiError(ApiError.builder().message("Filed 'status' is mandatory ").status(HttpStatus.BAD_REQUEST).build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_04(TaskManagementException.builder().apiError(ApiError.builder().message("Filed 'status' value is  " + Arrays.toString(Task.Status.values())).status(HttpStatus.BAD_REQUEST).build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_05(TaskManagementException.builder().apiError(ApiError.builder().status(HttpStatus.UNAUTHORIZED).message("The password you entered is incorrect. Please try again.").build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_06(TaskManagementException.builder().apiError(ApiError.builder().status(HttpStatus.UNAUTHORIZED).message("User Not Found").build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_07(TaskManagementException.builder().apiError(ApiError.builder().status(HttpStatus.NOT_FOUND).message("Task Not Found").build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_08(TaskManagementException.builder().apiError(ApiError.builder().status(HttpStatus.BAD_REQUEST).message("Task Id is Not Provided In Request Body").build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_09(TaskManagementException.builder().apiError(ApiError.builder().status(HttpStatus.BAD_REQUEST).message("DueDateFrom must added with DueDateTo & DueDateFrom must before DueDateTo").build()).build()),
    TASK_MANAGEMENT_EXCEPTION_ENUM_10(TaskManagementException.builder().apiError(ApiError.builder().status(HttpStatus.BAD_REQUEST).message("CreatedDateFrom must added with CreatedDateTo & CreatedDateFrom must before CreatedDateTo").build()).build());
    private final TaskManagementException value ;


}
