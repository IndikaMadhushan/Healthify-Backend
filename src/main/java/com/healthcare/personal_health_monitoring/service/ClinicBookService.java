package com.healthcare.personal_health_monitoring.service;


import com.healthcare.personal_health_monitoring.dto.ClinicBookRequestDTO;

public interface ClinicBookService {

    String  createClinicBook(Long patientId, ClinicBookRequestDTO clinicBookRequestDTO, Long doctorId);

    ClinicBookRequestDTO getReasonAndReason(int  id);

    String updateReason(ClinicBookRequestDTO clinicBookRequestDTO, int bookid,Long doctorId);

    String deleteClinicBook(int bookid,Long doctorId);
}