package com.example.userManagement.mapper;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserResponseDto;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "isDeleted", constant = "false")
    User toEntity(UserCreateRequest dto);

    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "deleted", target = "isDeleted")
    UserResponseDto toDto(User user);

//    @Mapping(target = "isActive", ignore = true) // Ignore isActive field to prevent overwriting
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // This will ignore null values in the DTO and leave the existing entity values unchanged.
    void updateUserFromDto(UserUpdateRequest dto, @MappingTarget User user);
}