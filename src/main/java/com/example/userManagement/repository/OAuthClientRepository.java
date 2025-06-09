// src/main/java/com/example/userManagement/repository/OAuthClientRepository.java
package com.example.userManagement.repository;

import com.example.userManagement.entity.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
    Optional<OAuthClient> findByClientId(String clientId);

    @Query("SELECT c FROM OAuthClient c LEFT JOIN FETCH c.redirectUris LEFT JOIN FETCH c.scopes WHERE c.clientId = :clientId")
    Optional<OAuthClient> findByClientIdWithDetails(@Param("clientId") String clientId);
}