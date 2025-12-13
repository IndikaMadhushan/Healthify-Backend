package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.NoteRepository;
import com.healthcare.personal_health_monitoring.repository.*;
import com.healthcare.personal_health_monitoring.service.FileUploadService;
import com.healthcare.personal_health_monitoring.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final FileUploadService fileUploadService;

    @Override
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    @Override
    public List<Note> getNotesByPatientId(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    @Override
    public List<Note> getNotesByPatientIdAndDateRange(Long patientId, LocalDate start, LocalDate end) {
        return noteRepository.findByPatientIdAndVisitDateBetween(patientId, start, end);
    }

    @Override
    public List<Note> getPatientHistory(Long patientId,
                                        LocalDate from,
                                        LocalDate to) {

        return noteRepository.findByPatientIdAndVisitDateBetween(
                patientId, from, to
        );
    }

        @Override
        public Note addNote (Long patientId, String description, MultipartFile file){

            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            // Get currently logged-in doctor
            Doctor doctor = (Doctor) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            Note note = new Note();
            note.setPatient(patient);
            note.setDoctor(doctor);
            note.setVisitDate(LocalDate.now());
            note.setDescription(description);

            if (file != null && !file.isEmpty()) {
                String url = fileUploadService.uploadFile(file);
                note.setFileUrl(url);
            }

            return noteRepository.save(note);
        }

}
