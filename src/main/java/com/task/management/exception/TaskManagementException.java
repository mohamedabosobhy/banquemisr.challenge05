package com.task.management.exception;

import com.task.management.model.ApiError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Getter
@Builder
public class TaskManagementException extends Exception{
    private ApiError apiError;

  public TaskManagementException(ApiError apiError)
  {
      this.apiError=apiError;
  }

}
