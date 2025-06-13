package com.example.userManagement.initialiser;

import com.example.userManagement.repository.JpaRegisteredClientRepository;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.repository.RedirectUriRepository;
import com.example.userManagement.repository.ClientScopeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import com.example.userManagement.repository.UserRepository;
import com.example.userManagement.entity.User;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.entity.ClientScope;

import java.util.UUID;
import java.time.LocalDate;

@Configuration
public class Initializer {
// http://localhost:8080/oauth2/authorize?response_type=code&client_id=imposter2&redirect_uri=http://localhost:8080/login/oauth2/code/imposter2&scope=read%20write
    @Bean
    public CommandLineRunner addAnotherClient(JpaRegisteredClientRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByClientId("leonardo") == null) {
                RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("leonardo")
                        .clientSecret(encoder.encode("leonardo"))
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .redirectUri("http://localhost:8080/login/oauth2/code/leonardo")
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
            if (userRepository.findByUsername("mayank").isEmpty()) {
                User user = new User();
                user.setFirstName("mayank");
                user.setMiddleName("A");
                user.setLastName("pandey");
                user.setEmail("ravi@example.com");
                user.setMobile("1234567890");
                user.setUsername("mayank");
                user.setPassword(encoder.encode("mayank"));
                user.setIsActive(true);
                user.setIsDeleted(false);
                user.setCreatedBy("system");
                user.setUpdatedBy("system");
                userRepository.save(user);

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