package com.example.userManagement.repository;

import com.example.userManagement.entity.RedirectUri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedirectUriRepository extends JpaRepository<RedirectUri, String> {
}