package com.example.userManagement.service.Interface;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.dto.user.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserResponseDto create(UserCreateRequest r);
    Optional<UserResponseDto> login(String username, String rawPwd);
    void update(Long id, UserUpdateRequest request);
    List<UserResponseDto> get();
    UserResponseDto getById(Long id);
    void delete(Long id);

}