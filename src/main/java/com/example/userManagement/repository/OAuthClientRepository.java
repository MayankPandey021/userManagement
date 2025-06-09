// src/main/java/com/example/userManagement/repository/OAuthClientRepository.java
package com.example.userManagement.repository;

import com.example.userManagement.entity.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
    Optional<OAuthClient> findByClientId(String clientId);
}