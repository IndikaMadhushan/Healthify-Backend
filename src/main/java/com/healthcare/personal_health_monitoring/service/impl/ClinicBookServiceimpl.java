package com.healthcare.personal_health_monitoring.service.impl;


import com.healthcare.personal_health_monitoring.dto.ClinicBookRequestDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicBookViewDTO;
import com.healthcare.personal_health_monitoring.entity.AccessControlClinic;
import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.Doctor;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.ClinicBookRepo;
import com.healthcare.personal_health_monitoring.repository.DoctorRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.ClinicBookService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

//    @Override
//    public List<ClinicBookViewDTO> getClinicBookDetails(long patientId) {
//        List<ClinicBook> cb = clinicBookRepo
//                .findByPatient_Id(patientId)
//                .orElseThrow(() -> new RuntimeException("Clinic book not found"));
//
//        Doctor doctor = cb.getDoctor(); // already available via relation
//
//        return new ClinicBookViewDTO(
//                doctor.getFullName(),
//                doctor.getSpecialization(),
//                doctor.getLicenseNumber(),
//                cb.getVisit_reason(),
//                cb.getAccessControl().name(),
//                cb.getUpdatedDoctor()== null
//                        ? "Not updated yet"
//                        : cb.getUpdatedDoctor().toString(),
//                cb.getUpdatedTime() == null
//                        ? "Not updated yet"
//                        : cb.getUpdatedTime().toString()
//        );
//    }

    @Override
    public List<ClinicBookViewDTO> getClinicBookDetails(long patientId) {

        List<ClinicBook> clinicBooks =
                clinicBookRepo.findAllByPatient_Id(patientId);

        if (clinicBooks.isEmpty()) {
            throw new RuntimeException("No clinic books found for patient " + patientId);
        }

        return clinicBooks.stream().map(cb -> {

            Doctor doctor = cb.getDoctor();

            return new ClinicBookViewDTO(
                    cb.getId(),

                    doctor != null ? doctor.getFullName() : "Not assigned",
                    doctor != null ? doctor.getSpecialization() : "N/A",
                    doctor != null ? doctor.getLicenseNumber() : "N/A",

                    cb.getVisit_reason(),
                    cb.getAccessControl().name(),

                    cb.getUpdatedDoctor() == null
                            ? "Not updated yet"
                            : cb.getUpdatedDoctor(),

                    cb.getUpdatedTime() == null
                            ? "Not updated yet"
                            : cb.getUpdatedTime().toString()
            );

        }).toList();
    }

    @Override
    public List<ClinicBookViewDTO> getPatientClinicBookDetails(long patientId) {
        List<ClinicBook> clinicBooks =
                clinicBookRepo.findAllByPatient_Id(patientId);

        if (clinicBooks.isEmpty()) {
            throw new RuntimeException("No clinic books found for patient " + patientId);
        }

        return clinicBooks.stream().map(cb -> {

            Doctor doctor = cb.getDoctor();

            return new ClinicBookViewDTO(
                    cb.getId(),
                    doctor != null ? doctor.getFullName() : "Not assigned",
                    doctor != null ? doctor.getSpecialization() : "N/A",
                    doctor != null ? doctor.getLicenseNumber() : "N/A",


                    cb.getVisit_reason(),
                    cb.getAccessControl().name(),



                    cb.getUpdatedDoctor() == null
                            ? "Not updated yet"
                            : cb.getUpdatedDoctor(),

                    cb.getUpdatedTime() == null
                            ? "Not updated yet"
                            : cb.getUpdatedTime().toString()
            );

        }).toList();
    }


//using test data
//    @Override
//    public ClinicBookViewDTO getClinicBookDetails(long patientId) {
//
//        ClinicBook cb = clinicBookRepo
//                .findByPatient_Id(patientId)
//                .orElseThrow(() -> new RuntimeException("Clinic book not found"));
//
//        System.out.println("ClinicBook found: " + cb.getId());
//
//        System.out.println("Doctor object: " + cb.getDoctor());
//        System.out.println("Patient object: " + cb.getPatient());
//        System.out.println("Visit reason: " + cb.getVisit_reason());
//        System.out.println("Access control: " + cb.getAccessControl());
//
//        return new ClinicBookViewDTO(
//                "TEST",
//                "TEST",
//                "TEST",
//                cb.getVisit_reason(),
//                cb.getAccessControl().name(),
//                "TEST",
//                "TEST"
//        );
//    }


}
