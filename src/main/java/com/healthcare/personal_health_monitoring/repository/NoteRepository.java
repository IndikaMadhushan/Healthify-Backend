package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByPatientIdAndVisitDateBetween(Long patientId, LocalDate startDate, LocalDate endDate);
    List<Note> findByPatientId(Long patientId);
   // Optional<Patient> findByEmail(String email);

}
