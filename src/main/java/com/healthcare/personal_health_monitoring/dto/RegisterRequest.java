package com.healthcare.personal_health_monitoring.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String role; // "PATIENT" or "DOCTOR"
    private String nic;
    // add other fields you want to accept at registration (phone, postalCode, etc.)

    // getters and setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
}
