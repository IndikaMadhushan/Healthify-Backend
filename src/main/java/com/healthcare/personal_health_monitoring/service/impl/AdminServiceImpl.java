package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Admin;
import com.healthcare.personal_health_monitoring.repository.AdminRepository;
import com.healthcare.personal_health_monitoring.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        adminOpt.ifPresent(admin -> {
            admin.setPassword(newPassword);
            adminRepository.save(admin);
        });
    }
}
