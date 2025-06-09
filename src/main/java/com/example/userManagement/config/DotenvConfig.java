package com.example.userManagement.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    private final Dotenv dotenv = Dotenv.load();



    @Bean
    public String userUsername() {
        return dotenv.get("USER_USERNAME");
    }

    @Bean
    public String userPassword() {
        return dotenv.get("USER_PASSWORD");
    }
}
