package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.entity.Surgery;
import com.healthcare.personal_health_monitoring.entity.Vaccination;
import com.healthcare.personal_health_monitoring.service.DoctorPatientHistoryService;
import com.healthcare.personal_health_monitoring.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/patients")
@RequiredArgsConstructor
public class DoctorPatientHistoryController {

    private final NoteService noteService;
    private final DoctorPatientHistoryService doctorPatientHistoryService;

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

    //view surgeries
    @GetMapping("/{patientId}/surgeries")
    public List<Surgery> getPatientSurgeries(@PathVariable Long patinetId) {
        return doctorPatientHistoryService.getSurgeries(patinetId);
    }

    //view vaccinations
    @GetMapping("/{patientId}/vaccinations")
    public List<Vaccination> getPatientVaccinations(@PathVariable Long patientId){
        return doctorPatientHistoryService.getVaccinations(patientId);
    }
}

