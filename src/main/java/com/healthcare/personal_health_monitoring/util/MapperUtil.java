//package com.healthcare.personal_health_monitoring.util;
//
//
//
//import com.healthcare.personal_health_monitoring.dto.*;
//import com.healthcare.personal_health_monitoring.entity.*;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class MapperUtil {
//
//    public static UserResponseDTO toUserResponse(User user) {
//        Set<String> diseaseNames = user.getDiseases() == null ? Set.of()
//                : user.getDiseases().stream().map(Disease::getName).collect(Collectors.toSet());
//
//        Set<String> allergyNames = user.getAllergies() == null ? Set.of()
//                : user.getAllergies().stream().map(Allergy::getName).collect(Collectors.toSet());
//
//        return UserResponseDTO.builder()
//                .id(user.getId())
//                .fullName(user.getFullName())
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .gender(user.getGender())
//                .dateOfBirth(user.getDateOfBirth())
//                .bloodGroup(user.getBloodGroup())
//                .height(user.getHeight())
//                .weight(user.getWeight())
//                .address(user.getAddress())
//                .city(user.getCity())
//                .district(user.getDistrict())
//                .province(user.getProvince())
//                .country(user.getCountry())
//                .emergencyContactName(user.getEmergencyContactName())
//                .emergencyContactPhone(user.getEmergencyContactPhone())
//                .emergencyContactRelationship(user.getEmergencyContactRelationship())
//                .diseases(diseaseNames)
//                .allergies(allergyNames)
//                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
//                .build();
////    }
//
//    public static DiseaseDTO toDiseaseDTO(Disease d) {
//        return DiseaseDTO.builder().id(d.getId()).name(d.getName()).build();
//    }
//
//    public static AllergyDTO toAllergyDTO(Allergy a) {
//        return AllergyDTO.builder().id(a.getId()).name(a.getName()).build();
//    }
//}
