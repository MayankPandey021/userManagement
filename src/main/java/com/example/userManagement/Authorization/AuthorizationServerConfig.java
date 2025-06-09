
package com.example.userManagement.Authorization;
import com.example.userManagement.security.JpaRegisteredClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;


@Configuration
public class AuthorizationServerConfig {

    private final String userUsername;
    private final String userPassword;

    @Autowired
    public AuthorizationServerConfig(
            @Qualifier("userUsername") String userUsername,
            @Qualifier("userPassword") String userPassword
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
        System.out.println(">> Auth Server Security Filter Chain is active");

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

//    @Bean
//    public UserDetailsService users(PasswordEncoder passwordEncoder) {
//        UserDetails user = User.withUsername(userUsername)
//                .password(passwordEncoder.encode(userPassword))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
}