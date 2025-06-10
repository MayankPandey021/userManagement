package com.example.userManagement.config;


import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;


import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Return the current user or a static string for testing
        return () -> Optional.of("system");
    }
}
