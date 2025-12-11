package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.repository.NoteRepository;
import com.healthcare.personal_health_monitoring.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

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
        return noteRepository.findByPatientIdAndDateBetween(patientId, start, end);
    }
}
