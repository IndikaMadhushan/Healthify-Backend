package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ClinicPageDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicPageResponseDTO;
import com.healthcare.personal_health_monitoring.dto.HealthMetricRequestSetDTO;
import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.*;
import com.healthcare.personal_health_monitoring.service.ClinicPageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClinicPageServiceimpl implements ClinicPageService {
    @Autowired
    private ClinicBookRepo clinicBookRepo;

    @Autowired
    private PatientHealthMetricRepository patientHealthMetricRepository;

    @Autowired
    private ClinicPageRepo clinicPageRepo;

    @Autowired
    private ModelMapper clinicPagemapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DoctorRepository doctorRepo;

    @Transactional
    @Override
    public String saveClinicPage(int clinicBookId, ClinicPageDTO clinicPageDTO, Long doctor_id) {


        ClinicBook clinicBook = clinicBookRepo.findById(clinicBookId)
                .orElseThrow(() -> new RuntimeException("ClinicBook not found"));

        if(clinicBook.getAccessControl().equals(AccessControlClinic.DENY)) {
            if (!clinicBook.getDoctor().getUser().getId().equals(doctor_id)) {
                throw new SecurityException("You are not allowed to add clinic pages to this clinic book");
            }
        }

        // Get Patient automatically
        Patient patient = clinicBook.getPatient();

        // Create ClinicPage
        ClinicPage clinicPage = new ClinicPage();
        clinicPage.setClinicBook(clinicBook);
        clinicPage.setSubReason(clinicPageDTO.getSubReason());
        clinicPage.setClinicExaming(clinicPageDTO.getClinicExaming());
        clinicPage.setClinicSuggestTest(clinicPageDTO.getClinicSuggestTest());
        clinicPage.setClinicDoctorNote(clinicPageDTO.getClinicDoctorNote());
        clinicPage.setNextClinic(clinicPageDTO.getNextClinic());


        List<Medication> meds = new ArrayList<>();

        if (clinicPageDTO.getMedication() != null &&
                !clinicPageDTO.getMedication().isEmpty()) {

            meds = clinicPageDTO.getMedication().stream().map(mdto -> {
                Medication m = new Medication();
                m.setClinicPage(clinicPage);
                m.setDrugName(mdto.getDrugName());
                m.setDosage(mdto.getDosage());
                m.setFrequency(mdto.getFrequency());
                m.setDuration(mdto.getDuration());
                m.setInstruction(mdto.getInstruction());
                return m;
            }).toList();
        }

        clinicPage.setMedication(meds);

        clinicPageRepo.save(clinicPage);

        if (clinicPageDTO.getHealthMetricRequestSetDTO() != null &&
                clinicPageDTO.getHealthMetricRequestSetDTO().getMetrics() != null) {

            for (Map.Entry<HealthMetricType, Double> entry :
                    clinicPageDTO.getHealthMetricRequestSetDTO().getMetrics().entrySet()) {

                Double value = entry.getValue();
                if (value == null) continue;

                PatientHealthMetric metric = new PatientHealthMetric();
                metric.setPatient(patient);
                metric.setMetricType(entry.getKey());
                metric.setValue(value);
                metric.setPageType(PageType.CLINIC);
                metric.setPageId(clinicPage.getClinicPageId());

                patientHealthMetricRepository.save(metric);
            }
        }

        return "CREATED SUCCESSFULLY";

    }

    @Transactional
    public String updateClinicPage(int clinicPageId,ClinicPageDTO clinicPageDTO,Long doctor_id) {


        ClinicPage clinicPage = clinicPageRepo.findById(clinicPageId)
                .orElseThrow(() -> new RuntimeException("Clinic page not found"));
        ClinicBook clinicBook = clinicPage.getClinicBook();

        if(clinicBook.getAccessControl().equals(AccessControlClinic.DENY)) {
            if (!clinicPage.getClinicBook().getDoctor().getUser().getId().equals(doctor_id)) {
                throw new RuntimeException("You cannot edit this clinic page");
            }
        }

        Doctor doctor = doctorRepo.findById(doctor_id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Check time/approval
        boolean withinTime = isWithinEditWindow(clinicPage);
        boolean approved = clinicPage.isPatientApprovedForEdit();

        if (!withinTime && !approved) {
            throw new RuntimeException(
                    "Edit time expired. Patient approval required.Do you want request?"
            );
        }

        // uPDATE DATA (ALLOWED)
        clinicPage.setUpdatedDoctor(doctor.getFullName()); //should reaplace with slmc no
        clinicPage.setSubReason(clinicPageDTO.getSubReason());
        clinicPage.setClinicExaming(clinicPageDTO.getClinicExaming());
        clinicPage.setClinicSuggestTest(clinicPageDTO.getClinicSuggestTest());
        clinicPage.setClinicDoctorNote(clinicPageDTO.getClinicDoctorNote());
        clinicPage.setNextClinic(clinicPageDTO.getNextClinic());

        //  Replace medications safely
        clinicPage.getMedication().clear();

        if (clinicPageDTO.getMedication() != null) {
            List<Medication> meds = clinicPageDTO.getMedication().stream().map(mdto -> {
                Medication m = new Medication();
                m.setClinicPage(clinicPage);
                m.setDrugName(mdto.getDrugName());
                m.setDosage(mdto.getDosage());
                m.setFrequency(mdto.getFrequency());
                m.setDuration(mdto.getDuration());
                m.setInstruction(mdto.getInstruction());
                return m;
            }).toList();

            clinicPage.getMedication().addAll(meds);
        }

        // Reset approval (ONE-TIME USE)
        clinicPage.setPatientApprovedForEdit(false);
        clinicPage.setPatientApprovedTime(null);

        clinicPageRepo.save(clinicPage);

        //  UPDATE METRICS WITHOUT DELETE
        if (clinicPageDTO.getHealthMetricRequestSetDTO() != null &&
                clinicPageDTO.getHealthMetricRequestSetDTO().getMetrics() != null) {

            // 1Load existing metrics
            List<PatientHealthMetric> existingMetrics =
                    patientHealthMetricRepository.findByPageId(clinicPage.getClinicPageId());

            //  Map by metric type
            Map<HealthMetricType, PatientHealthMetric> metricMap =
                    existingMetrics.stream()
                            .collect(Collectors.toMap(
                                    PatientHealthMetric::getMetricType,
                                    m -> m
                            ));

            // Loop DTO metrics
            for (Map.Entry<HealthMetricType, Double> entry :
                    clinicPageDTO.getHealthMetricRequestSetDTO().getMetrics().entrySet()) {

                if (entry.getValue() == null) continue;

                PatientHealthMetric metric = metricMap.get(entry.getKey());

                if (metric != null) {
                    // UPDATE
                    metric.setValue(entry.getValue());
                    patientHealthMetricRepository.save(metric);
                } else {
                    // INSERT
                    PatientHealthMetric newMetric = new PatientHealthMetric();
                    newMetric.setMetricType(entry.getKey());
                    newMetric.setValue(entry.getValue());
                    newMetric.setPageId(clinicPage.getClinicPageId());

                    patientHealthMetricRepository.save(newMetric);
                }
            }
        }

        return "UPDATED SUCCESSFULLY";
    }

    @Override
    @Transactional
    public String deleteClinicPage(int clinicPageId, Long doctorId) {

        ClinicPage clinicPage = clinicPageRepo.findById(clinicPageId)
                .orElseThrow(() -> new RuntimeException("Clinic page not found"));

        ClinicBook clinicBook = clinicPage.getClinicBook();

        if(clinicBook.getAccessControl().equals(AccessControlClinic.DENY)) {
            if (!clinicPage.getClinicBook()
                    .getDoctor()
                    .getUser()
                    .getId()
                    .equals(doctorId)) {
                throw new RuntimeException("You cannot delete this clinic page");
            }
        }
        boolean withinTime = isWithinEditWindow(clinicPage);
        boolean approved = clinicPage.isPatientApprovedForEdit();

        if (!withinTime && !approved) {
            throw new RuntimeException(
                    "Delete time expired. Patient approval required."
            );
        }
        patientHealthMetricRepository.deleteByPageTypeAndPageId(
                PageType.CLINIC,
                clinicPage.getClinicPageId()
        );
        clinicPageRepo.delete(clinicPage);

        return "DELETED SUCCESSFULLY";
    }

    @Override
    public ClinicPageDTO getClinicPageData(int clinicPageId) {

        //  Get clinic page
        // Get clinic page
        ClinicPage clinicPage = clinicPageRepo.findById(clinicPageId)
                .orElseThrow(() -> new RuntimeException("Clinic page not found"));

        // Map clinic page → DTO (basic fields + medications)
        ClinicPageDTO clinicPageDTO =
                clinicPagemapper.map(clinicPage, ClinicPageDTO.class);

        //  Get metrics using pageType + pageId
        List<PatientHealthMetric> metrics =
                patientHealthMetricRepository.findByPageTypeAndPageId(
                        PageType.CLINIC,
                        clinicPageId
                );

        //  Convert metrics → Map
        HealthMetricRequestSetDTO metricDTO = new HealthMetricRequestSetDTO();

        metricDTO.setMetrics(
                metrics.stream()
                        .collect(Collectors.toMap(
                                PatientHealthMetric::getMetricType,
                                PatientHealthMetric::getValue,
                                (oldVal, newVal) -> newVal   // safety if duplicates
                        ))
        );

        //  Attach metrics to clinic page DTO
        clinicPageDTO.setHealthMetricRequestSetDTO(metricDTO);

        return clinicPageDTO;
    }


    @Transactional
    public void requestEditApproval(int clinicPageId, Long doctorId) {

        ClinicPage page = clinicPageRepo.findById(clinicPageId)
                .orElseThrow(() -> new RuntimeException("ClinicPage not found"));
        ClinicBook clinicBook = page.getClinicBook();

        if(clinicBook.getAccessControl().equals(AccessControlClinic.DENY)) {
            if (!page.getClinicBook().getDoctor().getUser().getId().equals(doctorId)) {
                throw new SecurityException("You are not allowed to edit/delete");
            }
        }
        // reset previous approval
        page.setPatientApprovedForEdit(false);
        page.setPatientApprovedTime(null);

        clinicPageRepo.save(page);

        String patientEmail =
                page.getClinicBook()
                        .getPatient()
                        .getUser()
                        .getEmail();

        emailService.sendApprovalMail(patientEmail, clinicPageId);
    }



    //after patient clicks email link
    @Transactional
    public String approveEditByPatient(int clinicPageId) {

        ClinicPage page = clinicPageRepo.findById(clinicPageId)
                .orElseThrow(() -> new RuntimeException("Clinic page not found"));
        if (page.isPatientApprovedForEdit()) {
            return "ALREADY APPROVED";
        }

        page.setPatientApprovedForEdit(true);
        page.setPatientApprovedTime(LocalDateTime.now());

        clinicPageRepo.save(page);

        return "EDIT APPROVED BY PATIENT";
    }

    private boolean isWithinEditWindow(ClinicPage clinicPage) {

        LocalDateTime createdAt = LocalDateTime.of(
                clinicPage.getPagecreatedDate(),
                clinicPage.getPagecreatedTime()
        );

        return createdAt.plusMinutes(10)
                .isAfter(LocalDateTime.now());
    }







}
