package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ClinicBookViewDTO;
import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.ConsultPage;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.ConsultRepo;
import com.healthcare.personal_health_monitoring.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultServiceImpl implements ConsultService {

    @Autowired
    private ConsultRepo consultRepo;

    @Override
    public List<ConsultCardRensponseDTO> getClinicPagesByClinicBookId(Long patientId) {

        List<ConsultPage> consultPages =
                consultRepo.findAllByPatient_Id(patientId);

        if (consultPages.isEmpty()) {
            throw new RuntimeException(
                    "No consult pages found for patient " + patientId
            );
        }

        return consultPages.stream()
                .map(cp -> new ConsultCardRensponseDTO(
                        cp.getConsultId(),
                        cp.getDoctor().getFullName(),
                        cp.getDoctor().getLicenseNumber(),
//                        cp.getPatient(),
                        cp.getConsultReason(),
                        cp.getPagecreatedDate(),
                        cp.getPagecreatedTime(),
                        cp.getUpdatedDate(),
                        cp.getUpdatedTime()

                ))
                .toList();
    }




}
