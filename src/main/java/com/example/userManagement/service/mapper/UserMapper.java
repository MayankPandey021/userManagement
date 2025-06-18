package com.example.userManagement.service.mapper;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserResponseDto;
import com.example.userManagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toEntity(UserCreateRequest dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setMobile(dto.getMobile());
        user.setIsActive(true);
        user.setIsDeleted(false);
        return user;
    }


    default UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setIsActive(user.isActive());
        dto.setDeleted(user.isDeleted());
        return dto;
    }

}
