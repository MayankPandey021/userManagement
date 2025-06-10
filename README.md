# User Management System

## Overview
This project is a Spring Boot-based User Management System with OAuth2 Authorization Server capabilities. It provides secure user registration, authentication, password management, and OAuth2 client management. The system is designed for extensibility, security, and ease of onboarding for new developers.

---

## Architecture

### 1. **Backend Framework**
- **Spring Boot**: Main application framework.
- **Spring Security**: Handles authentication, authorization, and password encryption.
- **Spring Authorization Server**: Provides OAuth2 Authorization Server features.
- **JPA/Hibernate**: ORM for database access.

### 2. **Key Modules**
- **Entity Layer**: Contains JPA entities (`User`, `OAuthClient`, `RedirectUri`, `ClientScope`).
- **Repository Layer**: Spring Data JPA repositories for CRUD and custom queries.
- **Service Layer**: Business logic (e.g., `UserService`).
- **Controller Layer**: REST endpoints (e.g., `UserController`).
- **Security Layer**: Custom user details, password encoding, and security configuration.
- **Crypto Layer**: Attribute encryption for sensitive fields.
- **Initialiser**: (If present) Used for seeding initial data.


### 3. **Security**
- **Password Encryption**: BCrypt via `PasswordEncoder`.
- **Field Encryption**: AES encryption for sensitive DB fields using `EncryptionConverter`.
- **JWT**: Used for stateless authentication.
- **CSRF**: Disabled for API endpoints.
- **Role-based Access**: Basic user role setup, extensible for more roles.

### 4. **OAuth2 Support**
- **Registered Clients**: Managed via `OAuthClient` entity and repository.
- **Redirect URIs & Scopes**: Managed via `RedirectUri` and `ClientScope` entities.
- **Authorization Server**: Configured in `AuthorizationServerConfig`.

---

## Getting Started

### 1. **Prerequisites**
- Java 17+
- Maven 3.6+
- (Optional) Docker & Docker Compose for DB

### 2. **Setup**
1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd userManagement
   ```
2. **Configure Environment**
   - Copy `src/main/resources/env.properties` to `application.properties` or set environment variables as needed.
   - Set DB connection details, encryption keys, and OAuth2 credentials.
3. **Build the project**
   ```bash
   ./mvnw clean install
   ```
4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

### 3. **Database**
- Uses H2 (default) or configure for MySQL/Postgres in `application.properties`.
- JPA auto-creates tables based on entities.

### 4. **API Endpoints**
- **User APIs**: `/api/users/*` (register, login, list, get, reset-password, delete)
- **OAuth2 Endpoints**: Standard `/oauth2/*` endpoints for authorization server

### 5. **Testing**
- Use Postman or curl to test endpoints.
- Example: Register a user
  ```bash
  curl -X POST http://localhost:8080/api/users/create -H 'Content-Type: application/json' -d '{"firstName":"John", ...}'
  ```

---

## Code Structure

```
src/main/java/com/example/userManagement/
├── controller/         # REST controllers
├── dto/                # Data transfer objects
├── entity/             # JPA entities
├── exception/          # Global exception handling
├── initialiser/        # Data initialisation (if present)
├── repository/         # Spring Data JPA repositories
├── security/           # Security config, auth, crypto
├── service/            # Business logic
└── UserManagementApplication.java
```

---

## Onboarding Checklist
1. **Clone and build the project** (see above)
2. **Review the architecture** (see this README)
3. **Check `application.properties` for environment-specific settings**
4. **Familiarize with main modules** (see Code Structure)
5. **Run the app and test endpoints**
6. **Review `UserService`, `UserController`, and security configs for business logic and security flow**
7. **Check `EncryptionConverter` for field-level encryption logic**
8. **For OAuth2, review `AuthorizationServerConfig` and `OAuthClientRepository`**

---

## Common Tasks
- **Add a new user field**: Update `User` entity, DTOs, and migration scripts if needed.
- **Add a new API**: Create a new controller method and service logic.
- **Change encryption**: Update `EncryptionConverter` and re-encrypt data as needed.
- **Add OAuth2 client**: Use repository or initializer to add new clients.

---

## Troubleshooting
- **DB errors**: Check DB config in `application.properties`.
- **Security errors**: Ensure JWT tokens are used for protected endpoints.
- **Build errors**: Run `./mvnw clean install -U` to refresh dependencies.

---

## Contact
For questions, reach out to the project maintainer or check the code comments for guidance.

---

Welcome aboard! 🚀

