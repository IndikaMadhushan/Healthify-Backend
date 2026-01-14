package com.healthcare.personal_health_monitoring.util;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.*;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class PatientMapper {

    public static Patient toEntityFromCreate(PatientCreateRequest r) {
        if (r == null) return null;
        Patient p = new Patient();
        p.setFullName(r.getFullName());
        p.getUser().setEmail(r.getEmail());
        p.setNic(r.getNic());
        p.setDateOfBirth(r.getDateOfBirth());
        p.setGender(r.getGender());
        p.setHeight(r.getHeight());
        p.setWeight(r.getWeight());
        p.setBloodType(r.getBloodType());
        p.setPostalCode(r.getPostalCode());

        if (r.getPrimaryContact() != null) p.setPrimaryContact(toEmergencyEntity(r.getPrimaryContact()));
        if (r.getSecondaryContact() != null) p.setSecondaryContact(toEmergencyEntity(r.getSecondaryContact()));
        if (r.getFather() != null) p.setFather(toFamilyEntity(r.getFather()));
        if (r.getMother() != null) p.setMother(toFamilyEntity(r.getMother()));
        if (r.getSiblings() != null) p.setSiblings(r.getSiblings().stream().map(PatientMapper::toFamilyEntity).collect(Collectors.toList()));

        return p;
    }

    public static void mapUpdateToEntity(PatientUpdateRequest r, Patient p) {
        if (r == null || p == null) return;
        if (r.getFullName() != null) p.setFullName(r.getFullName());
        if (r.getEmail() != null) p.setEmail(r.getEmail());
        if (r.getNic() != null) p.setNic(r.getNic());
        if (r.getDateOfBirth() != null) p.setDateOfBirth(r.getDateOfBirth());
        if (r.getGender() != null) p.setGender(r.getGender());
        if (r.getHeight() != null) p.setHeight(r.getHeight());
        if (r.getWeight() != null) p.setWeight(r.getWeight());
        if (r.getBloodType() != null) p.setBloodType(r.getBloodType());
        if (r.getPostalCode() != null) p.setPostalCode(r.getPostalCode());

        if (r.getPrimaryContact() != null) p.setPrimaryContact(toEmergencyEntity(r.getPrimaryContact()));
        if (r.getSecondaryContact() != null) p.setSecondaryContact(toEmergencyEntity(r.getSecondaryContact()));
        if (r.getFather() != null) p.setFather(toFamilyEntity(r.getFather()));
        if (r.getMother() != null) p.setMother(toFamilyEntity(r.getMother()));
        if (r.getSiblings() != null) p.setSiblings(r.getSiblings().stream().map(PatientMapper::toFamilyEntity).collect(Collectors.toList()));
    }

    public static PatientResponse toResponse(Patient p) {
        if (p == null) return null;
        return PatientResponse.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .email(p.getEmail())
                .nic(p.getNic())
                .dateOfBirth(p.getDateOfBirth())
                .age(p.getAge())
                .gender(p.getGender())
                .height(p.getHeight())
                .weight(p.getWeight())
                .bloodType(p.getBloodType())
                .postalCode(p.getPostalCode())
                .primaryContact(toEmergencyDTO(p.getPrimaryContact()))
                .secondaryContact(toEmergencyDTO(p.getSecondaryContact()))
                .father(toFamilyDTO(p.getFather()))
                .mother(toFamilyDTO(p.getMother()))
                .siblings(p.getSiblings() == null ? null : p.getSiblings().stream().map(PatientMapper::toFamilyDTO).collect(Collectors.toList()))
                .diseases(p.getDiseases() == null ? Set.of() : p.getDiseases().stream().map(pd -> pd.getDisease().getName()).collect(Collectors.toSet()))
                .allergies(p.getAllergies() == null ? Set.of() : p.getAllergies().stream().map(pa -> pa.getAllergy().getName()).collect(Collectors.toSet()))
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    private static EmergencyContact toEmergencyEntity(EmergencyContactDTO d) {
        if (d == null) return null;
        EmergencyContact e = new EmergencyContact();
        e.setName(d.getName());
        e.setPhoneNumber(d.getPhoneNumber());
        e.setRelationship(d.getRelationship());
        return e;
    }

    private static EmergencyContactDTO toEmergencyDTO(EmergencyContact e) {
        if (e == null) return null;
        EmergencyContactDTO d = new EmergencyContactDTO();
        d.setName(e.getName());
        d.setPhoneNumber(e.getPhoneNumber());
        d.setRelationship(e.getRelationship());
        return d;
    }

    private static FamilyMember toFamilyEntity(FamilyMemberDTO d) {
        if (d == null) return null;
        FamilyMember f = new FamilyMember();
        f.setName(d.getName());
        f.setDob(d.getDob());
        f.setAlive(d.getAlive());
        f.setCauseOfDeath(d.getCauseOfDeath());
        f.setDiseases(d.getDiseases());
        return f;
    }

    private static FamilyMemberDTO toFamilyDTO(FamilyMember f) {
        if (f == null) return null;
        FamilyMemberDTO d = new FamilyMemberDTO();
        d.setName(f.getName());
        d.setDob(f.getDob());
        d.setAlive(f.getAlive());
        d.setCauseOfDeath(f.getCauseOfDeath());
        d.setDiseases(f.getDiseases());
        return d;
    }


    public static PatientSearchResponse toSearchResponse(Patient p){
        return new PatientSearchResponse(
                p.getId(),
                p.getPatientId(),
                p.getFullName(),
                p.getNic(),
                p.getGender()
        );
    }
}
