package com.task.management.mapper;

import com.task.management.entity.Task;
import com.task.management.model.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "user.username", target = "assignUser")
    @Mapping(source = "createdUser.username", target = "createdUser")
    @Mapping(source = "createdDate", target = "createdDate")
    TaskResponse taskToTaskResponse(Task task);

    List<TaskResponse> taskListToTaskResponseList(List<Task> tasks);
}
