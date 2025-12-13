package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Note;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NoteService {
    Note saveNote(Note note);
    Optional<Note> getNoteById(Long id);
    List<Note> getNotesByPatientId(Long patientId);
    List<Note> getNotesByPatientIdAndDateRange(Long patientId, LocalDate start, LocalDate end);
    Note addNote(Long patientId, String description, MultipartFile file);
    List<Note> getPatientHistory(Long patientId, LocalDate from, LocalDate to
    );

}
