package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.dto.DoctorNoteDTO;

import java.util.List;

public interface DoctorNoteService {
    List<DoctorNoteDTO>  getpatientViewNote(Long patientId);
}
