package com.example.userManagement.entity;

import jakarta.persistence.*;
import lombok.Data;



import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "client_scope")

@Data
public class ClientScope {
    @Id
    private String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference  //prevents infinite recursion
    private OAuthClient client;

    @Column(nullable = false, length = 255)
    private String scope;

    @Column(name = "created_by")
    private String createdBy;

    @Column
    private String updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name= "created_at")
    private LocalDate createdAt;

    @Column(name="updated_at")
    private LocalDate updatedAt;

    public ClientScope() {}


}
