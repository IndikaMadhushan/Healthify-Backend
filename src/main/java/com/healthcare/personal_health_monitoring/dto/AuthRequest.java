package com.healthcare.personal_health_monitoring.dto;

public class AuthRequest {
    private String email; // or username if you want
    private String password;

    // getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
