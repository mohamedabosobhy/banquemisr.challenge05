package com.task.management.mapper;

import com.task.management.entity.User;
import com.task.management.model.UserRespone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "email", target = "email")
    UserRespone userToUserResponse(User user);

    List<UserRespone> userListToUserResponseList(List<User> users);
}
