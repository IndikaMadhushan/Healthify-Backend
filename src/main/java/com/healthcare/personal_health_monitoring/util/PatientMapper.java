package com.healthcare.personal_health_monitoring.util;

import com.healthcare.personal_health_monitoring.dto.EmergencyContactDTO;
import com.healthcare.personal_health_monitoring.dto.FamilyMemberDTO;
import com.healthcare.personal_health_monitoring.dto.PatientCreateRequest;
import com.healthcare.personal_health_monitoring.dto.PatientResponse;
import com.healthcare.personal_health_monitoring.dto.PatientSearchResponse;
import com.healthcare.personal_health_monitoring.dto.PatientUpdateRequest;
import com.healthcare.personal_health_monitoring.entity.EmergencyContact;
import com.healthcare.personal_health_monitoring.entity.FamilyMember;
import com.healthcare.personal_health_monitoring.entity.Patient;

import java.util.Set;
import java.util.stream.Collectors;

public class PatientMapper {

    public static Patient toEntityFromCreate(PatientCreateRequest request) {
        if (request == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setSecondName(request.getSecondName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setNic(request.getNic());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setHeight(request.getHeight());
        patient.setWeight(request.getWeight());
        patient.setBloodType(request.getBloodType());
        patient.setPostalCode(request.getPostalCode());

        if (request.getFather() != null) {
            patient.setFather(toFamilyEntity(request.getFather()));
        }
        if (request.getMother() != null) {
            patient.setMother(toFamilyEntity(request.getMother()));
        }
        if (request.getSiblings() != null) {
            patient.setSiblings(request.getSiblings().stream()
                    .map(PatientMapper::toFamilyEntity)
                    .collect(Collectors.toList()));
        }

        return patient;
    }

    public static void mapUpdateToEntity(PatientUpdateRequest request, Patient patient) {
        if (request == null || patient == null) {
            return;
        }

        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName());
        }
        if (request.getSecondName() != null) {
            patient.setSecondName(request.getSecondName());
        }
        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }
        if (request.getNic() != null) {
            patient.setNic(request.getNic());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            patient.setGender(request.getGender());
        }
        if (request.getHeight() != null) {
            patient.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            patient.setWeight(request.getWeight());
        }
        if (request.getBloodType() != null) {
            patient.setBloodType(request.getBloodType());
        }
        if (request.getPostalCode() != null) {
            patient.setPostalCode(request.getPostalCode());
        }
        if (request.getNationality() != null) {
            patient.setNationality(request.getNationality());
        }

        if (request.getFather() != null) {
            patient.setFather(toFamilyEntity(request.getFather()));
        }
        if (request.getMother() != null) {
            patient.setMother(toFamilyEntity(request.getMother()));
        }
        if (request.getSiblings() != null) {
            patient.setSiblings(request.getSiblings().stream()
                    .map(PatientMapper::toFamilyEntity)
                    .collect(Collectors.toList()));
        }
    }

    public static PatientResponse toResponse(Patient patient) {
        if (patient == null) {
            return null;
        }

        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .secondName(patient.getSecondName())
                .lastName(patient.getLastName())
                .email(patient.getUser().getEmail())
                .enabled(patient.getUser().isEnabled())
                .role(patient.getUser().getRole().name())
                .registrationDate(patient.getUser().getCreatedAt().toLocalDate())
                .nic(patient.getNic())
                .dateOfBirth(patient.getDateOfBirth())
                .age(patient.getAge())
                .gender(patient.getGender())
                .height(patient.getHeight())
                .weight(patient.getWeight())
                .bloodType(patient.getBloodType())
                .postalCode(patient.getPostalCode())
                .primaryContact(toEmergencyDTO(patient.getPrimaryContact()))
                .secondaryContact(toEmergencyDTO(patient.getSecondaryContact()))
                .father(toFamilyDTO(patient.getFather()))
                .mother(toFamilyDTO(patient.getMother()))
                .siblings(patient.getSiblings() == null ? null : patient.getSiblings().stream()
                        .map(PatientMapper::toFamilyDTO)
                        .collect(Collectors.toList()))
                .diseases(patient.getDiseases() == null ? Set.of() : patient.getDiseases().stream()
                        .map(pd -> pd.getDisease().getName())
                        .collect(Collectors.toSet()))
                .allergies(patient.getAllergies() == null ? Set.of() : patient.getAllergies().stream()
                        .map(pa -> pa.getAllergy().getName())
                        .collect(Collectors.toSet()))
                .updatedAt(patient.getUpdatedAt())
                .patientId(patient.getPatientId())
                .photoUrl(patient.getPhotoUrl())
                .maritalStatus(patient.getMaritalStatus())
                .nationality(patient.getNationality())
                .occupation(patient.getOccupation())
                .district(patient.getDistrict())
                .address(patient.getAddress())
                .phone(patient.getPhone())
                .build();
    }

    private static EmergencyContact toEmergencyEntity(EmergencyContactDTO dto) {
        if (dto == null) {
            return null;
        }
        EmergencyContact contact = new EmergencyContact();
        contact.setName(dto.getName());
        contact.setPhoneNumber(dto.getPhoneNumber());
        contact.setRelationship(dto.getRelationship());
        return contact;
    }

    private static EmergencyContactDTO toEmergencyDTO(EmergencyContact contact) {
        if (contact == null) {
            return null;
        }
        EmergencyContactDTO dto = new EmergencyContactDTO();
        dto.setName(contact.getName());
        dto.setPhoneNumber(contact.getPhoneNumber());
        dto.setRelationship(contact.getRelationship());
        return dto;
    }

    private static FamilyMember toFamilyEntity(FamilyMemberDTO dto) {
        if (dto == null) {
            return null;
        }
        FamilyMember familyMember = new FamilyMember();
        familyMember.setName(dto.getName());
        familyMember.setDob(dto.getDob());
        familyMember.setAlive(dto.getAlive());
        familyMember.setCauseOfDeath(dto.getCauseOfDeath());
        familyMember.setDiseases(dto.getDiseases());
        return familyMember;
    }

    private static FamilyMemberDTO toFamilyDTO(FamilyMember familyMember) {
        if (familyMember == null) {
            return null;
        }
        FamilyMemberDTO dto = new FamilyMemberDTO();
        dto.setName(familyMember.getName());
        dto.setDob(familyMember.getDob());
        dto.setAlive(familyMember.getAlive());
        dto.setCauseOfDeath(familyMember.getCauseOfDeath());
        dto.setDiseases(familyMember.getDiseases());
        return dto;
    }

    public static PatientSearchResponse toSearchResponse(Patient patient) {
        return new PatientSearchResponse(
                patient.getId(),
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getSecondName(),
                patient.getLastName(),
                patient.getNic(),
                patient.getGender()
        );
    }
}
