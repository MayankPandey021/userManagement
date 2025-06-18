# User Management System

A comprehensive User Management System built with Java and Spring Boot. This project provides RESTful APIs and services for managing users, OAuth clients, client scopes, and redirect URIs. It is designed with a modular, layered architecture for scalability and maintainability.

## Features
- User registration and management
- OAuth client registration and management
- Client scope and redirect URI management
- Secure authentication and authorization
- Exception handling and validation

## Project Structure

```
src/
  main/
    java/com/example/userManagement/
      controller/      # REST controllers for handling HTTP requests
      dto/             # Data Transfer Objects for API communication
      entity/          # JPA entities representing database tables
      exception/       # Custom exceptions for error handling
      initialiser/     # Application/data initialization logic
      repository/      # Spring Data JPA repositories for DB access
      security/        # Security configuration and utilities
      service/         # Business logic and service layer
        implementation/ # Service implementation classes
        interface/      # Service interface definitions
        mapper/         # Mapper classes for entity-DTO conversion
    resources/
      application.properties  # Main application configuration
      env.properties          # Environment-specific configuration
      static/                 # Static web resources (if any)
      templates/              # Template files for web views (if any)
  test/
    java/com/example/userManagement/
      UserManagementApplicationTests.java  # Unit and integration tests
```

## Main Folders Explained
- **controller/**: Contains REST controllers that expose endpoints for user, client, scope, and redirect URI management.
- **dto/**: Data Transfer Objects used to transfer data between client, service, and persistence layers.
- **entity/**: JPA entity classes mapping to database tables.
- **exception/**: Custom exception classes for handling application-specific errors.
- **initialiser/**: Classes responsible for initializing data or application state at startup.
- **repository/**: Interfaces for database operations using Spring Data JPA.
- **security/**: Security configuration, authentication, and cryptography utilities.
- **service/**: Contains the business logic and service layer, organized as follows:
  - **implementation/**: Concrete implementations of service interfaces.
  - **interface/**: Service interface definitions for abstraction and dependency injection.
  - **mapper/**: Mapper classes for converting between entities and DTOs.

## Getting Started
1. **Build the project:**
   ```sh
   ./mvnw clean install
   ```
2. **Run the application:**
   ```sh
   ./mvnw spring-boot:run
   ```
3. **Access the API:**
   The application will start on `http://localhost:8080` by default.

## Configuration
- `application.properties` and `env.properties` contain application and environment-specific settings.

## Testing
- Unit and integration tests are located under `src/test/java/com/example/userManagement/`.

## License
This project is for learning and demonstration purposes.
