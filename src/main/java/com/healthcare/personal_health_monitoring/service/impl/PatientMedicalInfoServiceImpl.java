package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.PatientMedicalInfoDto;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.entity.PatientChronicCondition;
import com.healthcare.personal_health_monitoring.entity.PatientLifestyleInfo;
import com.healthcare.personal_health_monitoring.entity.PatientMedicalInfoProfile;
import com.healthcare.personal_health_monitoring.entity.PatientParentChronicCondition;
import com.healthcare.personal_health_monitoring.entity.PatientParentMedicalInfo;
import com.healthcare.personal_health_monitoring.entity.PatientVaccineSelection;
import com.healthcare.personal_health_monitoring.entity.Surgery;
import com.healthcare.personal_health_monitoring.repository.PatientChronicConditionRepository;
import com.healthcare.personal_health_monitoring.repository.PatientLifestyleInfoRepository;
import com.healthcare.personal_health_monitoring.repository.PatientMedicalInfoProfileRepository;
import com.healthcare.personal_health_monitoring.repository.PatientParentChronicConditionRepository;
import com.healthcare.personal_health_monitoring.repository.PatientParentMedicalInfoRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.repository.PatientVaccineSelectionRepository;
import com.healthcare.personal_health_monitoring.repository.SurgeryRepository;
import com.healthcare.personal_health_monitoring.service.PatientMedicalInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientMedicalInfoServiceImpl implements PatientMedicalInfoService {

    private final PatientRepository patientRepository;
    private final SurgeryRepository surgeryRepository;
    private final PatientMedicalInfoProfileRepository medicalInfoProfileRepository;
    private final PatientChronicConditionRepository patientChronicConditionRepository;
    private final PatientVaccineSelectionRepository patientVaccineSelectionRepository;
    private final PatientLifestyleInfoRepository patientLifestyleInfoRepository;
    private final PatientParentMedicalInfoRepository patientParentMedicalInfoRepository;
    private final PatientParentChronicConditionRepository patientParentChronicConditionRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientMedicalInfoDto getMedicalInfo(Long patientId) {
        getPatient(patientId);

        PatientMedicalInfoDto response = new PatientMedicalInfoDto();

        PatientMedicalInfoProfile medicalProfile = medicalInfoProfileRepository.findById(patientId).orElse(null);
        if (medicalProfile != null) {
            response.getMedicalInfo().getChronic().setOtherChronic(defaultString(medicalProfile.getOtherChronic()));
            response.getMedicalInfo().getChronic().setCancerChronic(defaultString(medicalProfile.getCancerChronic()));
            response.getMedicalInfo().getVaccines().setOtherVaccine(defaultString(medicalProfile.getOtherVaccine()));
        }

        response.getMedicalInfo().getChronic().setChronicIllnesses(
                patientChronicConditionRepository.findByPatientIdOrderByIdAsc(patientId).stream()
                        .map(PatientChronicCondition::getConditionName)
                        .toList()
        );

        response.getMedicalInfo().getVaccines().setTakenVaccines(
                patientVaccineSelectionRepository.findByPatientIdOrderByIdAsc(patientId).stream()
                        .map(PatientVaccineSelection::getVaccineName)
                        .toList()
        );

        response.getMedicalInfo().setSurgeries(
                surgeryRepository.findByPatientId(patientId).stream()
                        .map(this::toSurgeryItem)
                        .toList()
        );

        PatientLifestyleInfo lifestyleInfo = patientLifestyleInfoRepository.findById(patientId).orElse(null);
        if (lifestyleInfo != null) {
            response.getLifestyleAndAllergies().setSmokingStatus(defaultString(lifestyleInfo.getSmokingStatus()));
            response.getLifestyleAndAllergies().setSmokingFrequency(defaultString(lifestyleInfo.getSmokingFrequency()));
            response.getLifestyleAndAllergies().setAlcoholStatus(defaultString(lifestyleInfo.getAlcoholStatus()));
            response.getLifestyleAndAllergies().setAlcoholFrequency(defaultString(lifestyleInfo.getAlcoholFrequency()));
            response.getLifestyleAndAllergies().setDrugUseStatus(defaultString(lifestyleInfo.getDrugUseStatus()));
            response.getLifestyleAndAllergies().setDrugUseFrequency(defaultString(lifestyleInfo.getDrugUseFrequency()));
            response.getLifestyleAndAllergies().setStressLevel(defaultString(lifestyleInfo.getStressLevel()));
            response.getLifestyleAndAllergies().setFoodAllergies(defaultString(lifestyleInfo.getFoodAllergies()));
            response.getLifestyleAndAllergies().setDrugAllergies(defaultString(lifestyleInfo.getDrugAllergies()));
        }

        PatientParentMedicalInfo parentMedicalInfo = patientParentMedicalInfoRepository.findById(patientId).orElse(null);
        if (parentMedicalInfo != null) {
            response.getParentMedicalInfo().setOtherChronic(defaultString(parentMedicalInfo.getOtherChronic()));
        }

        response.getParentMedicalInfo().setChronicIllnesses(
                patientParentChronicConditionRepository.findByPatientIdOrderByIdAsc(patientId).stream()
                        .map(PatientParentChronicCondition::getConditionName)
                        .toList()
        );

        return response;
    }

    @Override
    public PatientMedicalInfoDto saveMedicalInfo(Long patientId, PatientMedicalInfoDto request) {
        Patient patient = getPatient(patientId);
        PatientMedicalInfoDto normalized = normalize(request);

        validate(normalized);

        saveMedicalInfoProfile(patient, normalized.getMedicalInfo().getChronic(), normalized.getMedicalInfo().getVaccines());
        replaceChronicConditions(patient, normalized.getMedicalInfo().getChronic().getChronicIllnesses());
        replaceVaccineSelections(patient, normalized.getMedicalInfo().getVaccines().getTakenVaccines());
        replaceSurgeries(patient, normalized.getMedicalInfo().getSurgeries());
        saveLifestyleInfo(patient, normalized.getLifestyleAndAllergies());
        saveParentMedicalInfo(patient, normalized.getParentMedicalInfo());

        return getMedicalInfo(patientId);
    }

    private Patient getPatient(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    private PatientMedicalInfoDto normalize(PatientMedicalInfoDto request) {
        PatientMedicalInfoDto normalized = request != null ? request : new PatientMedicalInfoDto();

        if (normalized.getMedicalInfo() == null) {
            normalized.setMedicalInfo(new PatientMedicalInfoDto.MedicalInfoSection());
        }
        if (normalized.getMedicalInfo().getChronic() == null) {
            normalized.getMedicalInfo().setChronic(new PatientMedicalInfoDto.ChronicSection());
        }
        if (normalized.getMedicalInfo().getVaccines() == null) {
            normalized.getMedicalInfo().setVaccines(new PatientMedicalInfoDto.VaccinesSection());
        }
        if (normalized.getMedicalInfo().getSurgeries() == null) {
            normalized.getMedicalInfo().setSurgeries(new ArrayList<>());
        }
        if (normalized.getLifestyleAndAllergies() == null) {
            normalized.setLifestyleAndAllergies(new PatientMedicalInfoDto.LifestyleAndAllergiesSection());
        }
        if (normalized.getParentMedicalInfo() == null) {
            normalized.setParentMedicalInfo(new PatientMedicalInfoDto.ParentMedicalInfoSection());
        }
        if (normalized.getParentMedicalInfo().getChronicIllnesses() == null) {
            normalized.getParentMedicalInfo().setChronicIllnesses(new ArrayList<>());
        }

        normalized.getMedicalInfo().getChronic().setChronicIllnesses(
                sanitizeLabels(normalized.getMedicalInfo().getChronic().getChronicIllnesses())
        );
        normalized.getMedicalInfo().getVaccines().setTakenVaccines(
                sanitizeLabels(normalized.getMedicalInfo().getVaccines().getTakenVaccines())
        );
        normalized.getParentMedicalInfo().setChronicIllnesses(
                sanitizeLabels(normalized.getParentMedicalInfo().getChronicIllnesses())
        );

        normalized.getMedicalInfo().getChronic().setOtherChronic(trimToEmpty(normalized.getMedicalInfo().getChronic().getOtherChronic()));
        normalized.getMedicalInfo().getChronic().setCancerChronic(trimToEmpty(normalized.getMedicalInfo().getChronic().getCancerChronic()));
        normalized.getMedicalInfo().getVaccines().setOtherVaccine(trimToEmpty(normalized.getMedicalInfo().getVaccines().getOtherVaccine()));
        normalized.getParentMedicalInfo().setOtherChronic(trimToEmpty(normalized.getParentMedicalInfo().getOtherChronic()));

        normalized.getLifestyleAndAllergies().setSmokingStatus(trimToEmpty(normalized.getLifestyleAndAllergies().getSmokingStatus()));
        normalized.getLifestyleAndAllergies().setSmokingFrequency(trimToEmpty(normalized.getLifestyleAndAllergies().getSmokingFrequency()));
        normalized.getLifestyleAndAllergies().setAlcoholStatus(trimToEmpty(normalized.getLifestyleAndAllergies().getAlcoholStatus()));
        normalized.getLifestyleAndAllergies().setAlcoholFrequency(trimToEmpty(normalized.getLifestyleAndAllergies().getAlcoholFrequency()));
        normalized.getLifestyleAndAllergies().setDrugUseStatus(trimToEmpty(normalized.getLifestyleAndAllergies().getDrugUseStatus()));
        normalized.getLifestyleAndAllergies().setDrugUseFrequency(trimToEmpty(normalized.getLifestyleAndAllergies().getDrugUseFrequency()));
        normalized.getLifestyleAndAllergies().setStressLevel(trimToEmpty(normalized.getLifestyleAndAllergies().getStressLevel()));
        normalized.getLifestyleAndAllergies().setFoodAllergies(trimToEmpty(normalized.getLifestyleAndAllergies().getFoodAllergies()));
        normalized.getLifestyleAndAllergies().setDrugAllergies(trimToEmpty(normalized.getLifestyleAndAllergies().getDrugAllergies()));

        ensureSelectedWhenTextPresent(normalized.getMedicalInfo().getChronic().getChronicIllnesses(), "Cancer", normalized.getMedicalInfo().getChronic().getCancerChronic());
        ensureSelectedWhenTextPresent(normalized.getMedicalInfo().getChronic().getChronicIllnesses(), "Other", normalized.getMedicalInfo().getChronic().getOtherChronic());
        ensureSelectedWhenTextPresent(normalized.getMedicalInfo().getVaccines().getTakenVaccines(), "Other", normalized.getMedicalInfo().getVaccines().getOtherVaccine());
        ensureSelectedWhenTextPresent(normalized.getParentMedicalInfo().getChronicIllnesses(), "Other", normalized.getParentMedicalInfo().getOtherChronic());

        normalized.getMedicalInfo().setSurgeries(
                normalized.getMedicalInfo().getSurgeries().stream()
                        .filter(Objects::nonNull)
                        .map(this::sanitizeSurgery)
                        .filter(surgery -> !isBlankSurgery(surgery))
                        .toList()
        );

        return normalized;
    }

    private void validate(PatientMedicalInfoDto request) {
        List<String> chronicIllnesses = request.getMedicalInfo().getChronic().getChronicIllnesses();
        if (containsLabel(chronicIllnesses, "Cancer") && isBlank(request.getMedicalInfo().getChronic().getCancerChronic())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cancerChronic is required when Cancer is selected");
        }
        if (containsLabel(chronicIllnesses, "Other") && isBlank(request.getMedicalInfo().getChronic().getOtherChronic())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "otherChronic is required when Other is selected");
        }
        if (containsLabel(request.getMedicalInfo().getVaccines().getTakenVaccines(), "Other")
                && isBlank(request.getMedicalInfo().getVaccines().getOtherVaccine())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "otherVaccine is required when Other is selected");
        }
        if (containsLabel(request.getParentMedicalInfo().getChronicIllnesses(), "Other")
                && isBlank(request.getParentMedicalInfo().getOtherChronic())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "parentMedicalInfo.otherChronic is required when Other is selected");
        }

        for (PatientMedicalInfoDto.SurgeryItem surgery : request.getMedicalInfo().getSurgeries()) {
            if (surgery.getSurgeryDate() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "surgeryDate is required for each surgery row");
            }
        }
    }

    private void saveMedicalInfoProfile(
            Patient patient,
            PatientMedicalInfoDto.ChronicSection chronic,
            PatientMedicalInfoDto.VaccinesSection vaccines
    ) {
        boolean hasValues = !isBlank(chronic.getOtherChronic())
                || !isBlank(chronic.getCancerChronic())
                || !isBlank(vaccines.getOtherVaccine());

        if (!hasValues) {
            medicalInfoProfileRepository.findById(patient.getId()).ifPresent(medicalInfoProfileRepository::delete);
            return;
        }

        PatientMedicalInfoProfile profile = medicalInfoProfileRepository.findById(patient.getId())
                .orElse(new PatientMedicalInfoProfile());
        profile.setPatient(patient);
        profile.setOtherChronic(emptyToNull(chronic.getOtherChronic()));
        profile.setCancerChronic(emptyToNull(chronic.getCancerChronic()));
        profile.setOtherVaccine(emptyToNull(vaccines.getOtherVaccine()));
        medicalInfoProfileRepository.save(profile);
    }

    private void replaceChronicConditions(Patient patient, List<String> chronicIllnesses) {
        patientChronicConditionRepository.deleteByPatientId(patient.getId());
        if (chronicIllnesses.isEmpty()) {
            return;
        }

        List<PatientChronicCondition> conditions = chronicIllnesses.stream()
                .map(label -> {
                    PatientChronicCondition condition = new PatientChronicCondition();
                    condition.setPatient(patient);
                    condition.setConditionName(label);
                    return condition;
                })
                .toList();

        patientChronicConditionRepository.saveAll(conditions);
    }

    private void replaceVaccineSelections(Patient patient, List<String> selectedVaccines) {
        patientVaccineSelectionRepository.deleteByPatientId(patient.getId());
        if (selectedVaccines.isEmpty()) {
            return;
        }

        List<PatientVaccineSelection> vaccines = selectedVaccines.stream()
                .map(label -> {
                    PatientVaccineSelection vaccine = new PatientVaccineSelection();
                    vaccine.setPatient(patient);
                    vaccine.setVaccineName(label);
                    return vaccine;
                })
                .toList();

        patientVaccineSelectionRepository.saveAll(vaccines);
    }

    private void replaceSurgeries(Patient patient, List<PatientMedicalInfoDto.SurgeryItem> surgeries) {
        List<Surgery> existingSurgeries = surgeryRepository.findByPatientId(patient.getId());
        if (!existingSurgeries.isEmpty()) {
            surgeryRepository.deleteAll(existingSurgeries);
        }

        if (surgeries.isEmpty()) {
            return;
        }

        List<Surgery> entities = surgeries.stream()
                .map(item -> {
                    Surgery surgery = new Surgery();
                    surgery.setPatient(patient);
                    surgery.setDescription(emptyToNull(item.getSurgeonName()));
                    surgery.setSurgeryDate(item.getSurgeryDate());
                    surgery.setHospital(emptyToNull(item.getHospital()));
                    surgery.setSugeryComplication(emptyToNull(item.getComplications()));
                    return surgery;
                })
                .toList();

        surgeryRepository.saveAll(entities);
    }

    private void saveLifestyleInfo(Patient patient, PatientMedicalInfoDto.LifestyleAndAllergiesSection lifestyle) {
        boolean hasValues = !isBlank(lifestyle.getSmokingStatus())
                || !isBlank(lifestyle.getSmokingFrequency())
                || !isBlank(lifestyle.getAlcoholStatus())
                || !isBlank(lifestyle.getAlcoholFrequency())
                || !isBlank(lifestyle.getDrugUseStatus())
                || !isBlank(lifestyle.getDrugUseFrequency())
                || !isBlank(lifestyle.getStressLevel())
                || !isBlank(lifestyle.getFoodAllergies())
                || !isBlank(lifestyle.getDrugAllergies());

        if (!hasValues) {
            patientLifestyleInfoRepository.findById(patient.getId()).ifPresent(patientLifestyleInfoRepository::delete);
            return;
        }

        PatientLifestyleInfo lifestyleInfo = patientLifestyleInfoRepository.findById(patient.getId())
                .orElse(new PatientLifestyleInfo());
        lifestyleInfo.setPatient(patient);
        lifestyleInfo.setSmokingStatus(emptyToNull(lifestyle.getSmokingStatus()));
        lifestyleInfo.setSmokingFrequency(emptyToNull(lifestyle.getSmokingFrequency()));
        lifestyleInfo.setAlcoholStatus(emptyToNull(lifestyle.getAlcoholStatus()));
        lifestyleInfo.setAlcoholFrequency(emptyToNull(lifestyle.getAlcoholFrequency()));
        lifestyleInfo.setDrugUseStatus(emptyToNull(lifestyle.getDrugUseStatus()));
        lifestyleInfo.setDrugUseFrequency(emptyToNull(lifestyle.getDrugUseFrequency()));
        lifestyleInfo.setStressLevel(emptyToNull(lifestyle.getStressLevel()));
        lifestyleInfo.setFoodAllergies(emptyToNull(lifestyle.getFoodAllergies()));
        lifestyleInfo.setDrugAllergies(emptyToNull(lifestyle.getDrugAllergies()));
        patientLifestyleInfoRepository.save(lifestyleInfo);
    }

    private void saveParentMedicalInfo(Patient patient, PatientMedicalInfoDto.ParentMedicalInfoSection parentMedicalInfo) {
        patientParentChronicConditionRepository.deleteByPatientId(patient.getId());

        if (!parentMedicalInfo.getChronicIllnesses().isEmpty()) {
            List<PatientParentChronicCondition> conditions = parentMedicalInfo.getChronicIllnesses().stream()
                    .map(label -> {
                        PatientParentChronicCondition condition = new PatientParentChronicCondition();
                        condition.setPatient(patient);
                        condition.setConditionName(label);
                        return condition;
                    })
                    .toList();
            patientParentChronicConditionRepository.saveAll(conditions);
        }

        if (isBlank(parentMedicalInfo.getOtherChronic())) {
            patientParentMedicalInfoRepository.findById(patient.getId()).ifPresent(patientParentMedicalInfoRepository::delete);
            return;
        }

        PatientParentMedicalInfo parentInfo = patientParentMedicalInfoRepository.findById(patient.getId())
                .orElse(new PatientParentMedicalInfo());
        parentInfo.setPatient(patient);
        parentInfo.setOtherChronic(emptyToNull(parentMedicalInfo.getOtherChronic()));
        patientParentMedicalInfoRepository.save(parentInfo);
    }

    private PatientMedicalInfoDto.SurgeryItem toSurgeryItem(Surgery surgery) {
        PatientMedicalInfoDto.SurgeryItem item = new PatientMedicalInfoDto.SurgeryItem();
        item.setSurgeonName(defaultString(surgery.getDescription()));
        item.setSurgeryDate(surgery.getSurgeryDate());
        item.setHospital(defaultString(surgery.getHospital()));
        item.setComplications(defaultString(surgery.getSugeryComplication()));
        return item;
    }

    private PatientMedicalInfoDto.SurgeryItem sanitizeSurgery(PatientMedicalInfoDto.SurgeryItem surgery) {
        surgery.setSurgeonName(trimToEmpty(surgery.getSurgeonName()));
        surgery.setHospital(trimToEmpty(surgery.getHospital()));
        surgery.setComplications(trimToEmpty(surgery.getComplications()));
        return surgery;
    }

    private boolean isBlankSurgery(PatientMedicalInfoDto.SurgeryItem surgery) {
        return isBlank(surgery.getSurgeonName())
                && surgery.getSurgeryDate() == null
                && isBlank(surgery.getHospital())
                && isBlank(surgery.getComplications());
    }

    private List<String> sanitizeLabels(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> unique = new LinkedHashSet<>();
        for (String label : labels) {
            String cleaned = trimToEmpty(label);
            if (!cleaned.isEmpty()) {
                unique.add(cleaned);
            }
        }
        return new ArrayList<>(unique);
    }

    private void ensureSelectedWhenTextPresent(List<String> labels, String expectedLabel, String supportingText) {
        if (!isBlank(supportingText) && !containsLabel(labels, expectedLabel)) {
            labels.add(expectedLabel);
        }
    }

    private boolean containsLabel(List<String> labels, String expectedLabel) {
        return labels.stream().anyMatch(label -> label.equalsIgnoreCase(expectedLabel));
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private String emptyToNull(String value) {
        String trimmed = trimToEmpty(value);
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String defaultString(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
