package com.example.userManagement.repository;

import com.example.userManagement.entity.ClientScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientScopeRepository extends JpaRepository<ClientScope, String> {
}