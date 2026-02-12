package com.healthcare.personal_health_monitoring.util;


import com.healthcare.personal_health_monitoring.dto.DoctorNoteDTO;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import com.healthcare.personal_health_monitoring.entity.ConsultPage;

public class DoctorNoteMapper {

    // ClinicPage → DTO
    public static DoctorNoteDTO fromClinicPage(ClinicPage page) {
        return new DoctorNoteDTO(
                page.getCreatedDoctor().getFullName(),
                page.getCreatedDoctor().getSpecialization(),
                page.getPagecreatedDate(),
                page.getPagecreatedTime(),
                page.getClinicDoctorNote()
        );
    }

    // ConsultPage → DTO
    public static DoctorNoteDTO fromConsultPage(ConsultPage page) {
        return new DoctorNoteDTO(
                page.getDoctor().getFullName(),
                page.getDoctor().getSpecialization(),
                page.getPagecreatedDate(),
                page.getPagecreatedTime(),
                page.getConsultDoctorNote()
        );
    }
}
