package com.example.userManagement.service.Interface;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.dto.user.UserResponseDto;

import com.example.userManagement.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserResponseDto createUser(UserCreateRequest r);

    Optional<UserResponseDto> login(String username, String rawPwd);
    void updateUser(Long id, UserUpdateRequest request);

    List<UserResponseDto> getUsers();


    UserResponseDto getUserById(Long id);



    void deleteUser(Long id);

}