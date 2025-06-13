package com.example.userManagement.service;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.entity.User;
import com.example.userManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired private PasswordEncoder encoder;

    public User createUser(UserCreateRequest r) {
        User u = new User();
        u.setFirstName(r.firstName);
        u.setMiddleName(r.middleName);
        u.setLastName(r.lastName);
        u.setEmail(r.email);
        u.setMobile(r.mobile);
        u.setUsername(r.username);
        u.setPassword(encoder.encode(r.password));
        u.setIsActive(false);
        u.setIsDeleted(false);
        u.setCreatedBy("admin");
        return repo.save(u);
    }

    public Optional<User> login(String username, String rawPwd) {
        return repo.findByUsername(username)
                .filter(u -> !u.isDeleted())
                .filter(u -> encoder.matches(rawPwd, u.getPassword()));
    }

    public  void updateUser(Long id, UserUpdateRequest request) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.firstName != null) user.setFirstName(request.firstName);
        if (request.middleName != null) user.setMiddleName(request.middleName);
        if (request.lastName != null) user.setLastName(request.lastName);
        if (request.email != null) user.setEmail(request.email);
        if (request.mobile != null) user.setMobile(request.mobile);
        if (request.username != null) user.setUsername(request.username);
        if (request.password != null) user.setPassword(encoder.encode(request.password)); // Assuming encryption

        repo.save(user);
    }


    public List<User> listUsers() {
       return repo.findAllNonDeletedUsers();
   }

    public User getUser(Long id) {
        return repo.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow();
    }

    public void resetPassword(String username, String newPassword) {

        User u = repo.findByUsername(username)
                .filter(user -> !user.isDeleted())
                .orElseThrow();

        u.setPassword(encoder.encode(newPassword));
        repo.save(u);
    }


    public void deleteUser(Long id) {

        User user = repo.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsDeleted(true);
        repo.save(user);

    }

}