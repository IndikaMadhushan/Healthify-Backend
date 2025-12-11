package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Admin;

import java.util.Optional;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    Optional<Admin> getAdminByUsername(String username);
    void changePassword(Long id, String newPassword);
}
