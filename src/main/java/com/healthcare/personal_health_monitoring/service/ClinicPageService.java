package com.healthcare.personal_health_monitoring.service;


import com.healthcare.personal_health_monitoring.dto.ClinicPageDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicPageViewDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicPrescriptionCardDTO;
import com.healthcare.personal_health_monitoring.dto.ClinicPrescriptionCardListDTO;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;

import java.util.List;

public interface ClinicPageService {
    String saveClinicPage(int clinicBookId, ClinicPageDTO clinicPageDTO, Long doctor_id);

    String approveEditByPatient(int clinicPageId);

    String updateClinicPage(int clinicPageId, ClinicPageDTO dto, Long doctor_id);

    ClinicPageDTO getClinicPageData(int clinicPageId);

    String deleteClinicPage(int clinicPageId,Long doctorId);

    void requestEditApproval(int clinicPageId,Long doctor_id);

    public List<ClinicPrescriptionCardDTO> getPagesByClinicBook(int clinicBookId);

    public List<ClinicPrescriptionCardListDTO> getClinicPagesByClinicBookId(int clinicBookId);
}
