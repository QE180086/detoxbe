package com.parttime.job.Application.projectmanagementservice.usermanagement.mapper;

import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role.name")
    UserResponse toUserDTO(User user);

    @Mapping(target = "role", source = "role.name")
    List<UserResponse> toListUserDTO(List<User> users);
}
