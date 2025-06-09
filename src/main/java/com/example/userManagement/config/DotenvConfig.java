//package com.example.userManagement.config;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MapPropertySource;
//import org.springframework.core.env.PropertySource;
//import jakarta.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class DotenvConfig {
//    private final Dotenv dotenv = Dotenv.load();
//    private final ConfigurableEnvironment environment;
//
//    public DotenvConfig(ConfigurableEnvironment environment) {
//        this.environment = environment;
//    }
//
//    @PostConstruct
//    public void addDotenvToEnvironment() {
//        Map<String, Object> dotenvProperties = new HashMap<>();
//        dotenv.entries().forEach(entry -> dotenvProperties.put(entry.getKey(), entry.getValue()));
//        PropertySource<?> propertySource = new MapPropertySource("dotenvProperties", dotenvProperties);
//        environment.getPropertySources().addLast(propertySource);
//    }
//}