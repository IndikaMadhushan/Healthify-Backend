package com.healthcare.personal_health_monitoring.service;


import com.healthcare.personal_health_monitoring.entity.IdSequence;
import com.healthcare.personal_health_monitoring.entity.SequenceType;
import com.healthcare.personal_health_monitoring.repository.IdSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class IdGeneratorService {

    private final IdSequenceRepository sequenceRepository;

    public String generatePatientCode() {
        IdSequence seq = sequenceRepository.findById(SequenceType.PATIENT)
                .orElseThrow(() -> new RuntimeException("Patient sequence missing"));

        Long value = seq.getNextValue();
        seq.setNextValue(value + 1);
        sequenceRepository.save(seq);

        int year = LocalDate.now().getYear();
        return String.format("PAT-%d-%06d", year, value);
    }

    public String generateDoctorCode() {
        IdSequence seq = sequenceRepository.findById(SequenceType.DOCTOR)
                .orElseThrow(() -> new RuntimeException("Doctor sequence missing"));

        Long value = seq.getNextValue();
        seq.setNextValue(value + 1);
        sequenceRepository.save(seq);

        return String.format("DOC-SL-%06d", value);
    }
}

