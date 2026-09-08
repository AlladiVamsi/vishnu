package com.plantcare.auth.dto;

public class AuthResponse {

    private UserResponse user;
    private String message;

    public AuthResponse() {}

    public AuthResponse(UserResponse user) {
        this.user = user;
        this.message = "Authenticated successfully";
    }

    public AuthResponse(UserResponse user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
