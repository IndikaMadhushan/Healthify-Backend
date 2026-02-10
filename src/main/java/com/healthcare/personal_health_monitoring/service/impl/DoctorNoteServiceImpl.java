package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.ConsultCardRensponseDTO;
import com.healthcare.personal_health_monitoring.dto.DoctorNoteDTO;
import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import com.healthcare.personal_health_monitoring.repository.ClinicBookRepo;
import com.healthcare.personal_health_monitoring.repository.ClinicPageRepo;
import com.healthcare.personal_health_monitoring.repository.ConsultRepo;
import com.healthcare.personal_health_monitoring.service.DoctorNoteService;
import com.healthcare.personal_health_monitoring.util.DoctorNoteMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorNoteServiceImpl implements DoctorNoteService {

    private ClinicPageRepo clinicPageRepo;
    private ConsultRepo consultRepo;

//    @Override
//    public List<DoctorNoteDTO> getpatientViewNote(Long patientId) {
//        ClinicBook clinicBook =
//                clinicBookRepo.findAllByPatient_Id(patientId);
//
//        List<ClinicPage> clinicPage = clinicPageRepo.findById(clinicBooks)
//                .orElseThrow(() -> new RuntimeException("Clinic page not found"));
//
//
//    }


    public DoctorNoteServiceImpl(ClinicPageRepo clinicPageRepo,
                             ConsultRepo consultPageRepo) {
        this.clinicPageRepo = clinicPageRepo;
        this.consultRepo = consultPageRepo;
    }

    public List<DoctorNoteDTO> getpatientViewNote(Long patientId) {

        List<DoctorNoteDTO> notes = new ArrayList<>();

        // Clinic notes
        clinicPageRepo.findByClinicBook_Patient_id(patientId)
                .forEach(page ->
                        notes.add(DoctorNoteMapper.fromClinicPage(page))
                );

        // Consult notes
        consultRepo.findByPatient_id(patientId)
                .forEach(page ->
                        notes.add(DoctorNoteMapper.fromConsultPage(page))
                );

        return notes;
    }



}
