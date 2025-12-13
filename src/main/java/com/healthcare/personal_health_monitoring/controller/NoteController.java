package com.healthcare.personal_health_monitoring.controller;

import com.healthcare.personal_health_monitoring.entity.Note;
import com.healthcare.personal_health_monitoring.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/doctor/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/{patientId}")
    public ResponseEntity<Note> addNote(
            @PathVariable Long patientId,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(
                noteService.addNote(patientId, description, file)
        );
    }
}

