
package com.example.userManagement.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;


// This configuration class sets up the authorization server for OAuth2
@Configuration
public class AuthorizationServerConfig {

    private final String userUsername;
    private final String userPassword;

    @Autowired
    public AuthorizationServerConfig(
            @Value("${USER_USERNAME}") String userUsername,
            @Value("${USER_PASSWORD}") String userPassword
    ) {
        this.userUsername = userUsername;
        this.userPassword = userPassword;
    }




    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(
            HttpSecurity http,
            @Qualifier("jpaRegisteredClientRepository") RegisteredClientRepository registeredClientRepository
    ) throws Exception {

        var authorizationServerConfigurer = new org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .with(authorizationServerConfigurer, configurer -> {});

        http.formLogin(withDefaults());

        return http.build();
    }

}