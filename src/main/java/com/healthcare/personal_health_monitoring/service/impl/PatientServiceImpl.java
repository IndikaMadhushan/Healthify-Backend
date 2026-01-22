package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.*;
import com.healthcare.personal_health_monitoring.entity.*;
import com.healthcare.personal_health_monitoring.repository.*;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import com.healthcare.personal_health_monitoring.service.PatientService;
import com.healthcare.personal_health_monitoring.util.AgeUtil;
import com.healthcare.personal_health_monitoring.util.BmiUtil;
import com.healthcare.personal_health_monitoring.util.PatientMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DiseaseRepository diseaseRepository;
    private final AllergyRepository allergyRepository;
    private final SurgeryRepository surgeryRepository;
    private final NoteRepository noteRepository;
    private final FileUploadService fileUploadService;

    @Override
    public PatientResponse createPatient(PatientCreateRequest request) {
        Patient p = PatientMapper.toEntityFromCreate(request);

        // age
        setAgeFromDob(p);

        // attach diseases
        if (request.getDiseaseIds() != null && !request.getDiseaseIds().isEmpty()) {
            List<PatientDisease> list = request.getDiseaseIds().stream()
                    .map(diseaseId -> {
                        Disease d = diseaseRepository.findById(diseaseId)
                                .orElseThrow(() -> new IllegalArgumentException("Disease not found: " + diseaseId));
                        PatientDisease pd = new PatientDisease();
                        pd.setDisease(d);
                        pd.setPatient(p);
                        return pd;
                    }).collect(Collectors.toList());
            p.setDiseases(new ArrayList<>(list));
        }

        // attach allergies
        if (request.getAllergyIds() != null && !request.getAllergyIds().isEmpty()) {
            List<PatientAllergy> list = request.getAllergyIds().stream()
                    .map(allergyId -> {
                        Allergy a = allergyRepository.findById(allergyId)
                                .orElseThrow(() -> new IllegalArgumentException("Allergy not found: " + allergyId));
                        PatientAllergy pa = new PatientAllergy();
                        pa.setAllergy(a);
                        pa.setPatient(p);
                        return pa;
                    }).collect(Collectors.toList());
            p.setAllergies(new ArrayList<>(list));
        }

        p.setUpdatedAt(LocalDateTime.now());

        Patient saved = patientRepository.save(p);
        return PatientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientUpdateRequest request) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));

        // map permitted fields
        PatientMapper.mapUpdateToEntity(request, patient);

        // recalc age if DOB changed
        if (request.getDateOfBirth() != null) setAgeFromDob(patient);

        // replace diseases if provided
        if (request.getDiseaseIds() != null) {
            List<PatientDisease> list = request.getDiseaseIds().stream()
                    .map(diseaseId -> {
                        Disease d = diseaseRepository.findById(diseaseId)
                                .orElseThrow(() -> new IllegalArgumentException("Disease not found: " + diseaseId));
                        PatientDisease pd = new PatientDisease();
                        pd.setDisease(d);
                        pd.setPatient(patient);
                        return pd;
                    }).collect(Collectors.toList());
            patient.setDiseases(new ArrayList<>(list));
        }

        // replace allergies if provided
        if (request.getAllergyIds() != null) {
            List<PatientAllergy> list = request.getAllergyIds().stream()
                    .map(allergyId -> {
                        Allergy a = allergyRepository.findById(allergyId)
                                .orElseThrow(() -> new IllegalArgumentException("Allergy not found: " + allergyId));
                        PatientAllergy pa = new PatientAllergy();
                        pa.setAllergy(a);
                        pa.setPatient(patient);
                        return pa;
                    }).collect(Collectors.toList());
            patient.setAllergies(new ArrayList<>(list));
        }
        //calculate age and set it

        if(request.getDateOfBirth() != null) {
            patient.setAge(AgeUtil.calculateAge(request.getDateOfBirth()));
        }

        // optional: link surgeries/notes by ids (replace)
        if (request.getSurgeryIds() != null) {
            List<Surgery> list = request.getSurgeryIds().stream()
                    .map(sid -> surgeryRepository.findById(sid).orElseThrow(() -> new IllegalArgumentException("Surgery not found: " + sid)))
                    .collect(Collectors.toList());
            patient.setSurgeries(new ArrayList<>(list));
        }

        if (request.getNoteIds() != null) {
            List<Note> list = request.getNoteIds().stream()
                    .map(nid -> noteRepository.findById(nid).orElseThrow(() -> new IllegalArgumentException("Note not found: " + nid)))
                    .collect(Collectors.toList());
            patient.setNotes(new ArrayList<>(list));
        }

        // calculate bmi value
        if (patient.getHeight() != null && patient.getWeight() != null) {
            patient.setBmi(
                    BmiUtil.calculateBmi(
                            patient.getWeight(),
                            patient.getHeight()
                    )
            );
        }

        patient.setUpdatedAt(LocalDateTime.now());
        Patient saved = patientRepository.save(patient);
        return PatientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient p = patientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
        return PatientMapper.toResponse(p);
    }

    @Override
    public PatientResponse getPatientByPatientId(String patientId) {
        Patient p = patientRepository.findByPatientId(patientId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient not found with patientId: " + patientId)
                );
        return PatientMapper.toResponse(p);
    }


    @Override
    public PatientResponse getPatientByEmail(String email) {
        Patient p = patientRepository.findByUserEmail(email).orElseThrow(() -> new IllegalArgumentException("Patient not found with email: " + email));
        return PatientMapper.toResponse(p);
    }

    @Override
    public PatientResponse getMe(String email) {
        return getPatientByEmail(email);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(PatientMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) throw new IllegalArgumentException("Patient not found: " + id);
        patientRepository.deleteById(id);
    }

    // helper
    private void setAgeFromDob(Patient p) {
        LocalDate dob = p.getDateOfBirth();
        if (dob != null) p.setAge(Period.between(dob, LocalDate.now()).getYears());
        else p.setAge(null);
    }

    public List<Patient> searchPatients(String query){

        //if the qury looks like generated patient id
        if(query.startsWith("PAT-")) {
            return patientRepository.findByPatientId(query)
                    .map(List::of)
                    .orElse(List.of());
        }

        //otherwise treat is as nic
        return  patientRepository.findByNicContainingIgnoreCase(query);
    }

    @Override
    public String uploadProfileImage(Long patientId, MultipartFile image) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        String imageUrl = fileUploadService.uploadFile(image);
        patient.setPhotoUrl(imageUrl);

        patientRepository.save(patient);
        return imageUrl;
    }

}
