package com.example.userManagement.service;

import com.example.userManagement.dto.UserCreateRequest;
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
        u.setInactive(false);
        u.setIsDeleted(false);
        u.setCreatedBy("admin");
        return repo.save(u);
    }

    public Optional<User> login(String username, String rawPwd) {
        return repo.findByUsername(username)
                .filter(u -> !u.isDeleted())
                .filter(u -> encoder.matches(rawPwd, u.getPassword()));
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