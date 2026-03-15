package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.*;
import com.healthcare.personal_health_monitoring.service.ConsultService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsultServiceImpl implements ConsultService {

    @Autowired
    private ConsultRepo consultRepo;

    @Autowired
    private PatientHealthMetricRepository patientHealthMetricRepository;

    @Autowired
    private MedicationRepo medicationRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

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
    public ConsultPageResponseDTO getConsultPageFullData(int consultId) {

        // 1️⃣ Get consult page
        ConsultPage consultPage = consultRepo.findById(consultId)
                .orElseThrow(() ->
                        new RuntimeException("Consult page not found"));

        ConsultPageResponseDTO dto = new ConsultPageResponseDTO();

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
        dto.setNextClinic(consultPage.getNextClinic());

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

    private boolean isWithinEditWindow(ConsultPage consultPage){

        LocalDateTime createdAt =
                LocalDateTime.of(
                        consultPage.getPagecreatedDate(),
                        consultPage.getPagecreatedTime()
                );

        return createdAt.plusMinutes(10)
                .isAfter(LocalDateTime.now());
    }
//
//    @Transactional
//    @Override
//    public String updateConsultPage(int consultId,
//                                    ConsultPageFullDTO dto,
//                                    Long doctorId){
//
//        ConsultPage consultPage =
//                consultRepo.findById(consultId)
//                        .orElseThrow(() ->
//                                new RuntimeException("Consult page not found"));
//
//        // ================= CHECK TIME =================
//
//        boolean withinTime = isWithinEditWindow(consultPage);
//
//        boolean approved = consultPage.isPatientApprovedForEdit();
//
//        if(!withinTime && !approved){
//
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN,
//                    "EDIT_WINDOW_EXPIRED"
//            );
//        }
//
//        // ================= UPDATE DATA =================
//
//        consultPage.setConsultReason(dto.getConsultReason());
//        consultPage.setConsultExaming(dto.getConsultExaming());
//        consultPage.setConsultSuggestTest(dto.getConsultSuggestTest());
//        consultPage.setConsultDoctorNote(dto.getConsultDoctorNote());
//        consultPage.setNextClinic(dto.getNextClinic());
//
//        consultRepo.save(consultPage);
//
//        // ================= REPLACE MEDICATION =================
//
//        medicationRepo.deleteByConsultPage_ConsultId(consultId);
//
//        if(dto.getMedications()!=null){
//
//            List<Medication> meds =
//                    dto.getMedications().stream().map(m->{
//
//                        Medication med = new Medication();
//
//                        med.setConsultPage(consultPage);
//                        med.setDrugName(m.getDrugName());
//                        med.setDosage(m.getDosage());
//                        med.setFrequency(m.getFrequency());
//                        med.setDuration(m.getDuration());
//                        med.setInstruction(m.getInstruction());
//
//                        return med;
//
//                    }).toList();
//
//            medicationRepo.saveAll(meds);
//        }
//
//        // reset approval after update
//        consultPage.setPatientApprovedForEdit(false);
//        consultPage.setPatientApprovedTime(null);
//
//        consultRepo.save(consultPage);
//
//        return "CONSULT PAGE UPDATED";
//    }

    @Transactional
    @Override
    public String updateConsultPage(int consultId,
                                    ConsultPageFullDTO dto,
                                    Long doctorId){

        ConsultPage consultPage =
                consultRepo.findById(consultId)
                        .orElseThrow(() ->
                                new RuntimeException("Consult page not found"));

        // ================= CHECK TIME / APPROVAL =================

        boolean withinTime = isWithinEditWindow(consultPage);
        boolean approved = consultPage.isPatientApprovedForEdit();

        if(!withinTime && !approved){

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "EDIT_WINDOW_EXPIRED"
            );
        }

        // ================= UPDATE CONSULT DATA =================

        consultPage.setConsultReason(dto.getConsultReason());
        consultPage.setConsultExaming(dto.getConsultExaming());
        consultPage.setConsultSuggestTest(dto.getConsultSuggestTest());
        consultPage.setConsultDoctorNote(dto.getConsultDoctorNote());
        consultPage.setNextClinic(dto.getNextClinic());

        consultRepo.save(consultPage);

        // ================= REPLACE MEDICATION =================

        medicationRepo.deleteByConsultPage_ConsultId(consultId);

        if(dto.getMedications()!=null){

            List<Medication> meds =
                    dto.getMedications().stream().map(m->{

                        Medication med = new Medication();

                        med.setConsultPage(consultPage);
                        med.setDrugName(m.getDrugName());
                        med.setDosage(m.getDosage());
                        med.setFrequency(m.getFrequency());
                        med.setDuration(m.getDuration());
                        med.setInstruction(m.getInstruction());

                        return med;

                    }).toList();

            medicationRepo.saveAll(meds);
        }

        // ================= UPDATE HEALTH METRICS =================

        if(dto.getHealthMetrics() != null){

            List<PatientHealthMetric> existingMetrics =
                    patientHealthMetricRepository
                            .findByPageTypeAndPageId(PageType.CONSULT, consultId);

            Map<HealthMetricType, PatientHealthMetric> metricMap =
                    existingMetrics.stream()
                            .collect(Collectors.toMap(
                                    PatientHealthMetric::getMetricType,
                                    m -> m
                            ));

            for (Map.Entry<HealthMetricType, Double> entry :
                    dto.getHealthMetrics().entrySet()) {

                if(entry.getValue() == null) continue;

                PatientHealthMetric metric =
                        metricMap.get(entry.getKey());

                if(metric != null){

                    // UPDATE EXISTING
                    metric.setValue(entry.getValue());
                    patientHealthMetricRepository.save(metric);

                }else{

                    // INSERT NEW
                    PatientHealthMetric newMetric =
                            new PatientHealthMetric();

                    newMetric.setMetricType(entry.getKey());
                    newMetric.setValue(entry.getValue());
                    newMetric.setPageType(PageType.CONSULT);
                    newMetric.setPageId(consultPage.getConsultId());
                    newMetric.setPatient(consultPage.getPatient());

                    patientHealthMetricRepository.save(newMetric);
                }
            }
        }

        // ================= RESET PATIENT APPROVAL =================

        consultPage.setPatientApprovedForEdit(false);
        consultPage.setPatientApprovedTime(null);

        consultRepo.save(consultPage);

        return "CONSULT PAGE UPDATED";
    }
//
//    @Transactional
//    @Override
//    public String deleteConsultPage(int consultId, Long doctorId){
//
//        ConsultPage consultPage =
//                consultRepo.findById(consultId)
//                        .orElseThrow(() ->
//                                new RuntimeException("Consult page not found"));
//
//        boolean withinTime = isWithinEditWindow(consultPage);
//
//        boolean approved = consultPage.isPatientApprovedForEdit();
//
//        if(!withinTime && !approved){
//
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN,
//                    "DELETE_WINDOW_EXPIRED_REQUEST_APPROVAL"
//            );
//        }
//
//        medicationRepo.deleteByConsultPage_ConsultId(consultId);
//
//        patientHealthMetricRepository
//                .deleteByPageTypeAndPageId(
//                        PageType.CONSULT,
//                        consultId
//                );
//
//        consultRepo.delete(consultPage);
//
//        return "CONSULT PAGE DELETED";
//    }

    @Transactional
    @Override
    public String deleteConsultPage(int consultId, Long doctorId){

        // ================= FIND CONSULT PAGE =================
        ConsultPage consultPage =
                consultRepo.findById(consultId)
                        .orElseThrow(() ->
                                new RuntimeException("Consult page not found"));

        // ================= CHECK EDIT WINDOW =================
        boolean withinTime = isWithinEditWindow(consultPage);
        boolean approved = consultPage.isPatientApprovedForEdit();

        if(!withinTime && !approved){

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "DELETE_WINDOW_EXPIRED_REQUEST_APPROVAL"
            );
        }

        // ================= DELETE MEDICATION =================
        medicationRepo.deleteByConsultPage_ConsultId(consultId);

        // ================= DELETE HEALTH METRICS =================
        patientHealthMetricRepository.deleteByPageTypeAndPageId(
                PageType.CONSULT,
                consultId
        );

        // ================= DELETE CONSULT PAGE =================
        consultRepo.delete(consultPage);

        return "CONSULT PAGE DELETED";
    }

    @Transactional
    public void requestEditApproval(int consultId){

        ConsultPage page =
                consultRepo.findById(consultId)
                        .orElseThrow(() ->
                                new RuntimeException("Consult page not found"));

        page.setPatientApprovedForEdit(false);
        page.setPatientApprovedTime(null);

        consultRepo.save(page);

        String patientEmail =
                page.getPatient()
                        .getUser()
                        .getEmail();

        emailService.sendApprovalMail(patientEmail, consultId);
    }
    @Transactional
    public String approveEditByPatient(int consultId){

        ConsultPage page =
                consultRepo.findById(consultId)
                        .orElseThrow(() ->
                                new RuntimeException("Consult page not found"));

        if(page.isPatientApprovedForEdit()){
            return "ALREADY APPROVED";
        }

        page.setPatientApprovedForEdit(true);
        page.setPatientApprovedTime(LocalDateTime.now());

        consultRepo.save(page);

        return "CONSULT EDIT APPROVED";
    }

//    @Transactional
//    public String createConsultPage(Long patientId,
//                                    ConsultPageFullDTO dto,
//                                    Long doctorId) {
//
//        Doctor doctor = doctorRepo.findById(doctorId)
//                .orElseThrow(() -> new RuntimeException("Doctor not found"));
//
//        Patient patient = patientRepo.findById(patientId)
//                .orElseThrow(() -> new RuntimeException("Patient not found"));
//
//        ConsultPage consultPage = new ConsultPage();
//
//        consultPage.setDoctor(doctor);
//        consultPage.setPatient(patient);
//        consultPage.setConsultReason(dto.getConsultReason());
//        consultPage.setConsultExaming(dto.getConsultExaming());
//        consultPage.setConsultSuggestTest(dto.getConsultSuggestTest());
//        consultPage.setConsultDoctorNote(dto.getConsultDoctorNote());
//
//        consultRepo.save(consultPage);
//
//        return "CONSULT PAGE CREATED";
//    }
//
//    @Transactional
//    public String createConsultPage(Long patientId,
//                                    ConsultPageFullDTO dto,
//                                    Long doctorId) {
//
//        Doctor doctor = doctorRepo.findById(doctorId)
//                .orElseThrow(() -> new RuntimeException("Doctor not found"));
//
//        Patient patient = patientRepo.findById(patientId)
//                .orElseThrow(() -> new RuntimeException("Patient not found"));
//
//        ConsultPage consultPage = new ConsultPage();
//
//        consultPage.setDoctor(doctor);
//        consultPage.setPatient(patient);
//
//        consultPage.setConsultReason(dto.getConsultReason());
//        consultPage.setConsultExaming(dto.getConsultExaming());
//        consultPage.setConsultSuggestTest(dto.getConsultSuggestTest());
//        consultPage.setConsultDoctorNote(dto.getConsultDoctorNote());
//        consultPage.setNextClinic(dto.getNextClinic());
//
//        consultRepo.save(consultPage);
//
//        return "CONSULT PAGE CREATED";
//    }

    @Transactional
    @Override
    public String createConsultPage(Long patientId,
                                    ConsultPageFullDTO dto,
                                    Long doctorId) {

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        boolean hasMedication = false;
        boolean hasMetrics = false;

        // ================= CREATE CONSULT PAGE =================

        ConsultPage consultPage = new ConsultPage();

        consultPage.setDoctor(doctor);
        consultPage.setPatient(patient);
        consultPage.setConsultReason(dto.getConsultReason());
        consultPage.setConsultExaming(dto.getConsultExaming());
        consultPage.setConsultSuggestTest(dto.getConsultSuggestTest());
        consultPage.setConsultDoctorNote(dto.getConsultDoctorNote());
        consultPage.setNextClinic(dto.getNextClinic());

        // ================= MEDICATION =================

        List<Medication> meds = new ArrayList<>();

        if (dto.getMedications() != null && !dto.getMedications().isEmpty()) {

            hasMedication = true;

            meds = dto.getMedications().stream().map(mdto -> {

                Medication m = new Medication();

                m.setConsultPage(consultPage);
                m.setDrugName(mdto.getDrugName());
                m.setDosage(mdto.getDosage());
                m.setFrequency(mdto.getFrequency());
                m.setDuration(mdto.getDuration());
                m.setInstruction(mdto.getInstruction());

                return m;

            }).collect(Collectors.toList());
        }

        consultPage.setMedicaion(meds);

        // ================= SAVE CONSULT PAGE =================

        consultRepo.save(consultPage);

        // ================= HEALTH METRICS =================

        if (dto.getHealthMetrics() != null && !dto.getHealthMetrics().isEmpty()) {

            hasMetrics = true;

            for (Map.Entry<HealthMetricType, Double> entry : dto.getHealthMetrics().entrySet()) {

                Double value = entry.getValue();

                if (value == null) continue;

                PatientHealthMetric metric = new PatientHealthMetric();

                metric.setPatient(patient);
                metric.setMetricType(entry.getKey());
                metric.setValue(value);
                metric.setPageType(PageType.CONSULT);
                metric.setPageId(consultPage.getConsultId());

                patientHealthMetricRepository.save(metric);
            }
        }

        // ================= RESPONSE =================

        if (hasMedication && hasMetrics) {
            return "Consult page + Medication + Health Metrics saved successfully";
        }
        else if (hasMedication) {
            return "Consult page + Medication saved successfully";
        }
        else if (hasMetrics) {
            return "Consult page + Health Metrics saved successfully";
        }
        else {
            return "Consult page saved successfully";
        }
    }
}
