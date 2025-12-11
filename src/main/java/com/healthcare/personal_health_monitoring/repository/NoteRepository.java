package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByPatientId(Long patientId);
    List<Note> findByPatientIdAndDateBetween(Long patientId, LocalDate start, LocalDate end);
}
