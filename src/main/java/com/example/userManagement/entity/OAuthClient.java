package com.example.userManagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;

@Entity
@Table(name = "oauth_client")
public class OAuthClient {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;



    @Column
    private String createdBy;

    @Column
    private String updatedBy;

    @Column
    private Boolean isDeleted;

    @Column
    private Boolean isActive;

    @Column
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;



    @Column(nullable = false)
    private String authorizationGrantTypes;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL,
//            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<RedirectUri> redirectUris = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL,
//            orphanRemoval = true,
            fetch = FetchType.LAZY) // // Lazy loading improves performance by loading scopes only when accessed
    private Set<ClientScope> scopes = new HashSet<>();

    public void setRedirectUris(Set<RedirectUri> redirectUris) {
        this.redirectUris = redirectUris;
    }

    // Getter
    public Set<RedirectUri> getRedirectUris() {
        return redirectUris;
    }
    public void setScopes(Set<ClientScope> scopes) {
        this.scopes = scopes;
    }

    // Getter
    public Set<ClientScope> getScopes() {
        return scopes;
    }
    public OAuthClient() {
        this.id = UUID.randomUUID().toString();
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }



    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }



}