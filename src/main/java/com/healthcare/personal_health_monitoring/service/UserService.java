package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.UserRequestDTO;
import com.healthcare.personal_health_monitoring.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRequestDTO req);
    UserResponseDTO getById(Long id);
    UserResponseDTO update(Long id, UserRequestDTO req);
    void addDiseaseToUser(Long userId, Long diseaseId);
    void addAllergyToUser(Long userId, Long allergyId);
}
