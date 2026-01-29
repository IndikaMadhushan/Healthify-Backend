package com.healthcare.personal_health_monitoring.service.impl;


import com.healthcare.personal_health_monitoring.dto.ClinicBookRequestDTO;
import com.healthcare.personal_health_monitoring.entity.AccessControlClinic;
import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.ClinicBookRepo;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.ClinicBookService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service

public class ClinicBookServiceimpl implements ClinicBookService {

//    @Autowired
//    private ClinicBookRepo clinicBookRepo;

    @Autowired
    private ModelMapper clinicBookmapper;

    @Autowired
    private ClinicBookRepo clinicBookRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Override
    public String createClinicBook(Long patientId, ClinicBookRequestDTO clinicBookRequestDTO, Long doctorId) {
//        ClinicBook clinicBook = clinicBookmapper.map(clinicBookRequestDTO,ClinicBook.class);
        ClinicBook clinicBook =new ClinicBook();
        clinicBook.setVisit_reason(clinicBookRequestDTO.getVisit_reason());
        clinicBook.setAccessControl(clinicBookRequestDTO.getAccessControl());

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));


        clinicBook.setDoctor(doctor);
        clinicBook.setPatient((patient));
        clinicBook.setCreatedTime(LocalTime.now());
        clinicBook.setCreatedDate(LocalDate.now());
        clinicBookRepo.save(clinicBook);

        return "SUCCESSFULLY";

    }

    @Override
    public ClinicBookRequestDTO getReasonAndReason(int id) {
        ClinicBook clinicBook = clinicBookRepo.getReferenceById(id);
        ClinicBookRequestDTO clinicBookRequestDTO = clinicBookmapper.map(clinicBook,ClinicBookRequestDTO.class);
        return clinicBookRequestDTO;
    }

    @Override
    public String updateReason(
            ClinicBookRequestDTO dto,
            int bookid,
            Long doctorId
    ) {

        ClinicBook clinicBook = clinicBookRepo.findById(bookid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Clinic book not found")
                );
        if(clinicBook.getAccessControl().equals(AccessControlClinic.DENY)) {
            if (!clinicBook.getDoctor().getUser().getId().equals(doctorId)) {
                throw new SecurityException("You are not allowed to edit this clinic book");
            }
        }

        Doctor doctor = doctorRepo.findById(doctorId)
             .orElseThrow(() -> new RuntimeException("Doctor not found"));

        clinicBook.setVisit_reason(dto.getVisit_reason());
        clinicBook.setAccessControl(dto.getAccessControl());
        clinicBook.setUpdatedDoctor(doctor.getFullName());  //should reaplace with slmc no
        clinicBook.setUpdatedTime(LocalDateTime.now());
        clinicBookRepo.save(clinicBook);

        return "UPDATED SUCCESSFULLY";
    }

    @Override
    public String deleteClinicBook(int bookid, Long doctorId) {

        ClinicBook clinicBook = clinicBookRepo.findById(bookid)
                .orElseThrow(() ->
                        new EntityNotFoundException("Clinic book not found")
                );

        if(clinicBook.getAccessControl().equals(AccessControlClinic.DENY)) {
            if (!clinicBook.getDoctor().getUser().getId().equals(doctorId)) {
                throw new SecurityException("You are not allowed to  delete this clinic book");
            }
        }

        clinicBookRepo.delete(clinicBook);
        return "DELETED SUCCESSFULLY";
    }
}
