//package com.healthcare.personal_health_monitoring.service.impl;
//
//import com.healthcare.personal_health_monitoring.dto.AllergyDTO;
//import com.healthcare.personal_health_monitoring.entity.Allergy;
//import com.healthcare.personal_health_monitoring.repository.AllergyRepository;
//import com.healthcare.personal_health_monitoring.service.AllergyService;
//import org.springframework.stereotype.Service;
//import jakarta.transaction.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class AllergyServiceImpl implements AllergyService {
//
//    private final AllergyRepository allergyRepository;
//
//    public AllergyServiceImpl(AllergyRepository allergyRepository) {
//        this.allergyRepository = allergyRepository;
//    }
//
//    @Override
//    public AllergyDTO create(String name) {
//        allergyRepository.findByNameIgnoreCase(name).ifPresent(a -> {
//            throw new IllegalArgumentException("Allergy already exists: " + name);
//        });
//        Allergy a = Allergy.builder().name(name).build();
//        Allergy saved = allergyRepository.save(a);
//        return AllergyDTO.builder().id(saved.getId()).name(saved.getName()).build();
//    }
//
//    @Override
//    public List<AllergyDTO> listAll() {
//        return allergyRepository.findAll().stream()
//                .map(a -> AllergyDTO.builder().id(a.getId()).name(a.getName()).build())
//                .collect(Collectors.toList());
//    }
//}
