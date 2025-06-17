package com.example.userManagement.initialiser;

import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.repository.JpaRegisteredClientRepository;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.repository.RedirectUriRepository;
import com.example.userManagement.repository.ClientScopeRepository;
import com.example.userManagement.service.implementation.UserService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.example.userManagement.dto.client.CreateClientRequest;

import com.example.userManagement.service.implementation.UserService;
import com.example.userManagement.dto.user.UserCreateRequest;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;

import com.example.userManagement.service.Implementation.OAuthClientService;

@Configuration
public class Initializer {

// http://localhost:8080/oauth2/authorize?response_type=code&client_id=imposter2&redirect_uri=http://localhost:8080/login/oauth2/code/imposter2&scope=read%20write

//
//    The commented-out code registers a client using JpaRegisteredClientRepository
//    and Spring's RegisteredClient model, which is compatible with Spring
//    Authorization Server's default client storage and secret validation.
//    Your new approach uses a custom OAuthClientService and a custom entity/model.
//    If your custom service does not store the client secret in a way that matches
//    how Spring Authorization Server expect
//            (i.e., encoded with the same PasswordEncoder and retrieved correctly),
//    authentication will fail.

    @Bean
    public CommandLineRunner addDefaultClient(JpaRegisteredClientRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByClientId("drake") == null) {
                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("drake")
                        .clientSecret(encoder.encode("drake"))
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://localhost:8080/login/oauth2/code/drake")
                        .scope("read")
                        .scope("write")

                        .build();
                repo.save(client);
            }
        };
    }


//    @Bean
//    public CommandLineRunner addDefaultClientWithService(OAuthClientService clientService, PasswordEncoder encoder) {
//        return args -> {
//            if (clientService.getClientByClientId("jake").isEmpty()) {
//                CreateClientRequest req = new CreateClientRequest();
//                req.setClientId("jake");
//                req.setClientSecret(encoder.encode("jake"));
//                req.setRedirectUris(List.of("http://localhost:8080/login/oauth2/code/jake"));
//                req.setScopes(List.of("read", "write"));
//                req.setAuthorizationGrantTypes(List.of("authorization_code", "refresh_token"));
//
//
//                clientService.createClient(req, "system"); // "system" or appropriate username
//            }
//        };
//    }


    @Bean
    public CommandLineRunner addDefaultUser(UserService userService) {
        return args -> {
            if (userService.findByUsername("mayank").isEmpty()) {
                UserCreateRequest req = new UserCreateRequest();
                req.setFirstName("mayank");
                req.setMiddleName("A");
                req.setLastName("pandey");
                req.setEmail("ravi@example.com");
                req.setMobile("1234567890");
                req.setUsername("mayank");
                req.setPassword("mayank");
                userService.createUser(req);
            }
        };
    }
}
