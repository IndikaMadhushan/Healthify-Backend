package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctor/patients")
@RequiredArgsConstructor
public class DoctorPatientHistoryController {

    private final NoteService noteService;

    @GetMapping("/{patientId}/history")
    public ResponseEntity<List<Note>> getPatientHistory(
            @PathVariable Long patientId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return ResponseEntity.ok(
                noteService.getPatientHistory(patientId, from, to)
        );
    }
}

