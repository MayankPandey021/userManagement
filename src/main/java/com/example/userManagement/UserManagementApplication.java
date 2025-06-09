package com.example.userManagement;

import com.example.userManagement.security.JpaRegisteredClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.UUID;

@SpringBootApplication
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

//
//	Store usernames in plain text in your database (do not encrypt them). This allows efficient user lookup during authentication.
//	Keep passwords hashed using your PasswordEncoder (e.g., BCrypt). This is already correct in your code.
//

//	Update your Spring Security configuration to use your custom UserDetailsService and PasswordEncoder.

//	@Bean
//	public CommandLineRunner addDefaultClient(JpaRegisteredClientRepository repo, PasswordEncoder encoder) {
//		return args -> {
//			if (repo.findByClientId("my-client") == null) {
//				RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
//						.clientId("my-client")
//						.clientSecret(encoder.encode("my-secret"))
//						.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//						.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//						.redirectUri("http://localhost:8080/login/oauth2/code/my-client")
//						.scope("read")
//						.scope("write")
//						.build();
//				repo.save(client);
//			}
//		};
//	}
}