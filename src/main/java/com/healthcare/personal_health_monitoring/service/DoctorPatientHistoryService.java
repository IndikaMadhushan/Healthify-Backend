package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.entity.Surgery;
import com.healthcare.personal_health_monitoring.entity.Vaccination;

import java.util.List;

public interface DoctorPatientHistoryService {
    List<Surgery> getSurgeries(Long patientId);

    List<Vaccination> getVaccinations(Long patientId);

}
