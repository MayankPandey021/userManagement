package com.example.userManagement.Authorization;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

import java.util.UUID;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
public class AuthorizationServerConfig {
   // You can’t see in-memory clients as files because they live only inside your app’s RAM.
    // in production, you would typically use a database  for registered clients.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("my-client")
                .clientSecret(passwordEncoder.encode("my-secret"))
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .redirectUri("http://localhost:8080/login/oauth2/code/my-client")
                .scope("read")
                .scope("write")
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        System.out.println(">> Auth Server Security Filter Chain is active");

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher) // very important!
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .with(authorizationServerConfigurer, configurer -> {});

        http.formLogin(withDefaults());

        return http.build();
    }



    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("testuser")
                .password(passwordEncoder.encode("testpass"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }


}
