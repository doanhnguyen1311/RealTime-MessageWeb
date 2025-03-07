package com.example.identity_service.mapper;

import com.example.identity_service.dto.request.UserCreatingRequest;
import com.example.identity_service.dto.request.UserRequest;
import com.example.identity_service.dto.request.UserUpdateRequest;
import com.example.identity_service.dto.response.UserResponse;
import com.example.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toUser(UserRequest userRequest);
    public UserResponse toUserResponse(User user);
    public UserUpdateRequest toUserUpdate(User user);

    @Mapping(target = "roles", ignore = true)
    void createUser(@MappingTarget User userR, UserCreatingRequest user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User userR, UserUpdateRequest user);

}
