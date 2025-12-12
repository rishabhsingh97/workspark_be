# Workspark Authentication Service

This project is a Spring Boot-based authentication service that supports user registration, sign-in, token management (JWT and refresh tokens), and password recovery. It is designed for integration with a broader system requiring secure authentication mechanisms.

## Features

- **User Registration**: Sign up new users with encrypted passwords.
- **User Sign-In**: Validate user credentials and provide JWT-based authentication.
- **Token Refresh**: Support for refreshing expired JWT tokens using a refresh token.
- **Password Recovery**: Allow users to reset their passwords using a reset token or change passwords through their existing credentials.
- **Global Exception Handling**: Handles various exceptions like invalid credentials, expired refresh tokens, and password reset failures.
- **Redis Integration**: Stores user sessions in Redis to manage authentication across different services.

## Technologies Used

- **Spring Boot**: A framework for building the backend services.
- **JWT (JSON Web Tokens)**: For generating secure access tokens.
- **Redis**: For storing user sessions.
- **BCrypt**: For password encryption and validation.
- **Spring Security**: For securing the application.
- **Lombok**: To reduce boilerplate code.
- **Jakarta Validation**: For request validation annotations.

## Project Structure

The project is divided into the following key packages:

- `com.workspark.exceptionhandler`: Global exception handling for custom exceptions.
- `com.workspark.exceptions`: Custom exceptions related to authentication and password recovery.
- `com.workspark.model`: Data models like `AuthenticationResponse`, `SignupRequest`, `ChangePasswordRequest`, etc.
- `com.workspark.service`: Interfaces and implementations for authentication and password recovery services.
- `com.workspark.util`: Utility classes for JWT generation and validation.

## Setting Up

1. **Clone the repository**:
    ```bash
    git clone https://gitlab.mindfire.co.in/rewards/api/authenticationservice.git
    ```

2. **Install dependencies**:
   Ensure you have Maven or Gradle installed, then run:
    ```bash
    mvn clean install
    ```

3. **Redis**:
   Ensure you have a Redis server running on your local machine or use a hosted Redis service.

4. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

### Authentication

- **POST /auth/signup**  
  Registers a new user.
  - **Request Body**:  
    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "username": "john_doe",
      "email": "john.doe@example.com",
      "password": "password123"
    }
    ```

- **POST /auth/signin**  
  Authenticates a user and returns JWT and refresh token.
  - **Request Body**:  
    ```json
    {
      "username": "john_doe",
      "password": "password123"
    }
    ```

- **POST /auth/refresh**  
  Refreshes an expired JWT token using a refresh token.
  - **Request Body**:  
    ```json
    {
      "refreshToken": "your-refresh-token"
    }
    ```

### Password Recovery

- **POST /auth/reset-password**  
  Resets the user's password using a reset token.
  - **Request Body**:  
    ```json
    {
      "resetToken": "your-reset-token",
      "newPassword": "newPassword123"
    }
    ```

- **POST /auth/change-password**  
  Changes the user's password using the old password and a new one.
  - **Request Body**:  
    ```json
    {
      "username": "john_doe",
      "oldPassword": "password123",
      "newPassword": "newPassword123"
    }
    ```

## Error Handling

The application uses a global exception handler to provide clear error messages:

- **401 Unauthorized**: Invalid credentials (e.g., wrong username or password).
- **403 Forbidden**: Expired refresh token.
- **400 Bad Request**: Invalid arguments or password reset errors.

### Example Error Response

```json
{
  "message": null,
  "data": null,
  "error": "Invalid credentials"
}
