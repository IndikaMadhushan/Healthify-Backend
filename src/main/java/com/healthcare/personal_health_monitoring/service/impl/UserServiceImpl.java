package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.UserRequestDTO;
import com.healthcare.personal_health_monitoring.dto.UserResponseDTO;
import com.healthcare.personal_health_monitoring.entity.Allergy;
import com.healthcare.personal_health_monitoring.entity.Disease;
import com.healthcare.personal_health_monitoring.entity.User;
import com.healthcare.personal_health_monitoring.repository.AllergyRepository;
import com.healthcare.personal_health_monitoring.repository.DiseaseRepository;
import com.healthcare.personal_health_monitoring.repository.UserRepository;
import com.healthcare.personal_health_monitoring.service.UserService;
import com.healthcare.personal_health_monitoring.util.MapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DiseaseRepository diseaseRepository;
    private final AllergyRepository allergyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           DiseaseRepository diseaseRepository,
                           AllergyRepository allergyRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.diseaseRepository = diseaseRepository;
        this.allergyRepository = allergyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO register(UserRequestDTO req) {
        // check existing email
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .gender(req.getGender())
                .dateOfBirth(req.getDateOfBirth())
                .bloodGroup(req.getBloodGroup())
                .height(req.getHeight())
                .weight(req.getWeight())
                .address(req.getAddress())
                .city(req.getCity())
                .district(req.getDistrict())
                .province(req.getProvince())
                .country(req.getCountry())
                .emergencyContactName(req.getEmergencyContactName())
                .emergencyContactPhone(req.getEmergencyContactPhone())
                .emergencyContactRelationship(req.getEmergencyContactRelationship())
                .build();

        // attach diseases if provided
        if (req.getDiseaseIds() != null && !req.getDiseaseIds().isEmpty()) {
            Set<Disease> diseases = new HashSet<>();
            for (Long did : req.getDiseaseIds()) {
                Disease d = diseaseRepository.findById(did)
                        .orElseThrow(() -> new IllegalArgumentException("Disease id not found: " + did));
                diseases.add(d);
            }
            user.setDiseases(diseases);
        }

        // attach allergies if provided
        if (req.getAllergyIds() != null && !req.getAllergyIds().isEmpty()) {
            Set<Allergy> allergies = new HashSet<>();
            for (Long aid : req.getAllergyIds()) {
                Allergy a = allergyRepository.findById(aid)
                        .orElseThrow(() -> new IllegalArgumentException("Allergy id not found: " + aid));
                allergies.add(a);
            }
            user.setAllergies(allergies);
        }

        User saved = userRepository.save(user);
        return MapperUtil.toUserResponse(saved);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
        return MapperUtil.toUserResponse(u);
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        // update fields (do not overwrite password unless provided)
        u.setFullName(req.getFullName() != null ? req.getFullName() : u.getFullName());
        u.setPhone(req.getPhone() != null ? req.getPhone() : u.getPhone());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        u.setGender(req.getGender() != null ? req.getGender() : u.getGender());
        u.setDateOfBirth(req.getDateOfBirth() != null ? req.getDateOfBirth() : u.getDateOfBirth());
        u.setBloodGroup(req.getBloodGroup() != null ? req.getBloodGroup() : u.getBloodGroup());
        u.setHeight(req.getHeight() != null ? req.getHeight() : u.getHeight());
        u.setWeight(req.getWeight() != null ? req.getWeight() : u.getWeight());
        u.setAddress(req.getAddress() != null ? req.getAddress() : u.getAddress());
        u.setCity(req.getCity() != null ? req.getCity() : u.getCity());
        u.setDistrict(req.getDistrict() != null ? req.getDistrict() : u.getDistrict());
        u.setProvince(req.getProvince() != null ? req.getProvince() : u.getProvince());
        u.setCountry(req.getCountry() != null ? req.getCountry() : u.getCountry());
        u.setEmergencyContactName(req.getEmergencyContactName() != null ? req.getEmergencyContactName() : u.getEmergencyContactName());
        u.setEmergencyContactPhone(req.getEmergencyContactPhone() != null ? req.getEmergencyContactPhone() : u.getEmergencyContactPhone());
        u.setEmergencyContactRelationship(req.getEmergencyContactRelationship() != null ? req.getEmergencyContactRelationship() : u.getEmergencyContactRelationship());

        // Optionally handle disease/allergy ID lists (replace current sets)
        if (req.getDiseaseIds() != null) {
            Set<Disease> diseases = new HashSet<>();
            for (Long did : req.getDiseaseIds()) {
                Disease d = diseaseRepository.findById(did)
                        .orElseThrow(() -> new IllegalArgumentException("Disease id not found: " + did));
                diseases.add(d);
            }
            u.setDiseases(diseases);
        }

        if (req.getAllergyIds() != null) {
            Set<Allergy> allergies = new HashSet<>();
            for (Long aid : req.getAllergyIds()) {
                Allergy a = allergyRepository.findById(aid)
                        .orElseThrow(() -> new IllegalArgumentException("Allergy id not found: " + aid));
                allergies.add(a);
            }
            u.setAllergies(allergies);
        }

        User saved = userRepository.save(u);
        return MapperUtil.toUserResponse(saved);
    }

    @Override
    public void addDiseaseToUser(Long userId, Long diseaseId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Disease d = diseaseRepository.findById(diseaseId).orElseThrow(() -> new IllegalArgumentException("Disease not found"));
        Set<Disease> set = u.getDiseases() == null ? new HashSet<>() : u.getDiseases();
        set.add(d);
        u.setDiseases(set);
        userRepository.save(u);
    }

    @Override
    public void addAllergyToUser(Long userId, Long allergyId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Allergy a = allergyRepository.findById(allergyId).orElseThrow(() -> new IllegalArgumentException("Allergy not found"));
        Set<Allergy> set = u.getAllergies() == null ? new HashSet<>() : u.getAllergies();
        set.add(a);
        u.setAllergies(set);
        userRepository.save(u);
    }
}
