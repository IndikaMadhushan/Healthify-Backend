package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ClinicBookViewDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultPageFullDTO;
import com.healthcare.personal_health_monitoring.dto.MedicationDTO;
import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.ConsultRepo;
import com.healthcare.personal_health_monitoring.repository.MedicationRepo;
import com.healthcare.personal_health_monitoring.repository.PatientHealthMetricRepository;
import com.healthcare.personal_health_monitoring.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultServiceImpl implements ConsultService {

    @Autowired
    private ConsultRepo consultRepo;

    @Autowired
    private PatientHealthMetricRepository patientHealthMetricRepository;

    @Autowired
    private MedicationRepo medicationRepo;

    @Override
    public List<ConsultCardRensponseDTO> getPatientPrescriptionCards(Long patientId) {

        List<ConsultPage> consultPages =
                consultRepo.findAllByPatient_Id(patientId);

        if (consultPages.isEmpty()) {
            throw new RuntimeException(
                    "No consult pages found for patient " + patientId
            );
        }

        return consultPages.stream()
                .map(cp -> new ConsultCardRensponseDTO(
                        cp.getConsultId(),
                        cp.getDoctor().getFullName(),
                        cp.getDoctor().getLicenseNumber(),
//                        cp.getPatient(),
                        cp.getConsultReason(),
                        cp.getPagecreatedDate(),
                        cp.getPagecreatedTime(),
                        cp.getUpdatedDate(),
                        cp.getUpdatedTime()

                ))
                .toList();
    }

    @Override
    public ConsultPageFullDTO getConsultPageFullData(int consultId) {

        // 1️⃣ Get consult page
        ConsultPage consultPage = consultRepo.findById(consultId)
                .orElseThrow(() ->
                        new RuntimeException("Consult page not found"));

        ConsultPageFullDTO dto = new ConsultPageFullDTO();

        // 2️⃣ Consult data
        dto.setConsultId(consultPage.getConsultId());
        dto.setConsultReason(consultPage.getConsultReason());
        dto.setConsultExaming(consultPage.getConsultExaming());
        dto.setConsultSuggestTest(consultPage.getConsultSuggestTest());
        dto.setConsultDoctorNote(consultPage.getConsultDoctorNote());
        dto.setCreatedDate(consultPage.getPagecreatedDate());
        dto.setCreatedTime(consultPage.getPagecreatedTime());
        dto.setUpdatedDate(consultPage.getUpdatedDate());
        dto.setUpdatedTime(consultPage.getUpdatedTime());

        // 3️⃣ Patient
        dto.setPatientName(consultPage.getPatient().getFullName());
        dto.setPatientGender(consultPage.getPatient().getGender());
        dto.setPatientAge(consultPage.getPatient().getAge());

        // 4️⃣ Doctor
        dto.setDoctorName(consultPage.getDoctor().getFullName());
        dto.setDoctorSpecialization(consultPage.getDoctor().getSpecialization());
        dto.setSlmc(consultPage.getDoctor().getLicenseNumber());

        // 5️⃣ Medications
        List<MedicationDTO> medicationDTOS =
                medicationRepo.findAllByConsultPage_ConsultId(consultId)
                        .stream()
                        .map(m -> {
                            MedicationDTO md = new MedicationDTO();
                            md.setDrugName(m.getDrugName());
                            md.setDosage(m.getDosage());
                            md.setFrequency(m.getFrequency());
                            md.setDuration(m.getDuration());
                            md.setInstruction(m.getInstruction());
                            return md;
                        })
                        .toList();

        dto.setMedications(medicationDTOS);

        // 6️⃣ Health metrics (CONSULT + pageId)
        List<PatientHealthMetric> metrics =
                patientHealthMetricRepository
                        .findByPageTypeAndPageId(PageType.CONSULT, consultId);

        dto.setHealthMetrics(
                metrics.stream()
                        .collect(Collectors.toMap(
                                PatientHealthMetric::getMetricType,
                                PatientHealthMetric::getValue,
                                (oldVal, newVal) -> newVal
                        ))
        );

        return dto;
    }




}
