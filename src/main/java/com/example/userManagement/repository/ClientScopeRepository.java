package com.example.userManagement.repository;

import com.example.userManagement.entity.ClientScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientScopeRepository extends JpaRepository<ClientScope, String> {
    List<ClientScope> findByClient_ClientIdAndIsDeletedFalse(String clientId);
    Optional<ClientScope> findByClient_ClientIdAndScopeAndIsDeletedFalse(String clientId, String scope);
    boolean existsByClient_ClientIdAndScopeAndIsDeletedFalse(String clientId, String scope);
    void deleteByClient_ClientId(String clientId);
}