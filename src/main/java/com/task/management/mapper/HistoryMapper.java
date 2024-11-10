package com.task.management.mapper;

import com.task.management.entity.History;
import com.task.management.entity.Task;
import com.task.management.model.HistoryResponse;
import com.task.management.model.TaskResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = TaskMapper.class)
public interface HistoryMapper {
    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userUpdated")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "taskId", target = "taskId")
    @Mapping(source = "description", target = "description")
    //@Mapping(target = "description",expression = "java(taskMapper.taskToTaskResponse(history.getTask()).toString())")
    HistoryResponse historyToHistoryResponse(History history);

    List<HistoryResponse> historyListToHistoryResponseList(List<History> histories);
}
