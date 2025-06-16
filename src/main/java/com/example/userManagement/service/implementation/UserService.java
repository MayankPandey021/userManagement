package com.example.userManagement.service.implementation;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.dto.user.UserResponseDto;

import com.example.userManagement.entity.User;
import com.example.userManagement.mapper.UserMapper;
import com.example.userManagement.repository.UserRepository;
import com.example.userManagement.service.abstraction.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.userManagement.dto.user.UserResponseDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private UserMapper mapper;

    @Override
    public UserResponseDto createUser(UserCreateRequest r) {
        User u = mapper.toEntity(r);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String createdBy = (auth != null) ? auth.getName() : "system";
        u.setPassword(encoder.encode(r.password));
        u.setCreatedBy(createdBy);
        User saved = repo.save(u);
        return mapper.toDto(saved);
    }


    @Override
    public Optional<User> login(String username, String rawPwd) {
        return repo.findByUsername(username)
                .filter(u -> !u.isDeleted())
                .filter(u -> encoder.matches(rawPwd, u.getPassword()));
    }

    @Override
    public void updateUser(Long id, UserUpdateRequest request) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only update fields from DTO, do not overwrite isActive/isDeleted/updatedBy
        mapper.updateUserFromDto(request, user);

        if (request.password != null) {
            user.setPassword(encoder.encode(request.password));
        }

        // Set updatedBy to current username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedBy = (auth != null) ? auth.getName() : "system";
        user.setUpdatedBy(updatedBy);

        repo.save(user);
    }



    @Override
    public List<UserResponseDto> getUsers() {
        return repo.findAllNonDeletedUsers()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = repo.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow();
        return mapper.toDto(user);
    }

    @Override
    public void resetPassword(String username, String newPassword) {
        User u = repo.findByUsername(username)
                .filter(user -> !user.isDeleted())
                .orElseThrow();

        u.setPassword(encoder.encode(newPassword));
        repo.save(u);
    }

    @Override
    public void deleteUser(Long id) {
        User user = repo.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsDeleted(true);
        repo.save(user);
    }
}