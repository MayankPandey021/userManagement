package com.example.userManagement.repository;

import com.example.userManagement.entity.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedirectUriRepository extends JpaRepository<RedirectUri, String> {
    List<RedirectUri> findByClient_ClientId(String clientId); // ✅ correct field

    List<RedirectUri> findAllByIsDeletedFalseAndIsActiveTrue();

    Optional<RedirectUri> findByClient_ClientIdAndUriAndIsDeletedFalse(String clientId, String uri);


}