package com.example.userManagement.initialiser;

import com.example.userManagement.security.JpaRegisteredClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import com.example.userManagement.repository.UserRepository;
import com.example.userManagement.entity.User;

import java.util.UUID;

@Configuration
public class Initializer {
// http://localhost:8080/oauth2/authorize?response_type=code&client_id=huge&redirect_uri=http://localhost:8080/login/oauth2/code/huge&scope=read%20write
    @Bean
    public CommandLineRunner addAnotherClient(JpaRegisteredClientRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByClientId("raj") == null) {
                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("raj")
                        .clientSecret(encoder.encode("raj"))
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://localhost:8080/login/oauth2/code/raj")
                        .scope("read")
                        .scope("write")
                        .build();
                repo.save(client);
            }
        };
    }

    @Bean
    public CommandLineRunner addDefaultUser(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("ravi").isEmpty()) {
                User user = new User();
                user.setFirstName("ravi");
                user.setMiddleName("A");
                user.setLastName("ravi");
                user.setEmail("ravi@example.com");
                user.setMobile("1234567890");
                user.setUsername("ravi");
                user.setPassword(encoder.encode("ravi"));
                user.setInactive(false);
                user.setIsDeleted(false);
                user.setCreatedBy("system");
                user.setUpdatedBy("system");
                userRepository.save(user);
                System.out.println("Checking for admin user...");
//                if (userRepository.findByUsername("admin") == null) {
//                    System.out.println("Admin user not found, creating...");
//                    // ... create and save user ...
//                    System.out.println("Admin user created.");
//                } else {
//                    System.out.println("Admin user already exists.");
//                }
            }
        };
    }

//    @Bean
//    public CommandLineRunner addDefaultClient(JpaRegisteredClientRepository repo, PasswordEncoder encoder) {
//        return args -> {
//            if (repo.findByClientId("my-client") == null) {
//                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
//                        .clientId("my-client")
//                        .clientSecret(encoder.encode("my-secret"))
//                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                        .redirectUri("http://localhost:8080/login/oauth2/code/my-client") // <-- required
//                        .scope("read")
//                        .scope("write")
//                        .build();
//                repo.save(client);
//            }
//        };
//    }
}