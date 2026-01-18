package com.healthcare.personal_health_monitoring.service.impl;

import com.healthcare.personal_health_monitoring.dto.AppointmentReminderRequest;
import com.healthcare.personal_health_monitoring.dto.AppointmentReminderResponse;
import com.healthcare.personal_health_monitoring.entity.AppointmentReminder;
import com.healthcare.personal_health_monitoring.entity.Patient;
import com.healthcare.personal_health_monitoring.repository.AppointmentReminderRepository;
import com.healthcare.personal_health_monitoring.repository.PatientRepository;
import com.healthcare.personal_health_monitoring.service.AppointmentReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentReminderServiceImpl implements AppointmentReminderService {

    private final AppointmentReminderRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Override
    public AppointmentReminderResponse addAppointment(Long patientId, AppointmentReminderRequest request) {

        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("patient not found"));

        AppointmentReminder reminder = new AppointmentReminder();

        reminder.setPatient(patient);
        reminder.setAppointmentDate(request.getAppointmentDate());
        reminder.setAppointmentTime(request.getAppointmentTime());
        reminder.setHospital(request.getHospital());
        reminder.setDoctorName(request.getDoctorName());
        reminder.setNote(request.getNote());

        AppointmentReminder saved = appointmentRepository.save(reminder);

        return new AppointmentReminderResponse(
                saved.getId(),
                saved.getAppointmentDate(),
                saved.getAppointmentTime(),
                saved.getHospital(),
                saved.getDoctorName(),
                saved.getNote(),
                saved.isCompleted()
        );
    }

    @Override
    public List<AppointmentReminderResponse> getAppointment(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(a -> new AppointmentReminderResponse(
                        a.getId(),
                        a.getAppointmentDate(),
                        a.getAppointmentTime(),
                        a.getHospital(),
                        a.getDoctorName(),
                        a.getNote(),
                        a.isCompleted()
                )).toList();
    }

    @Override
    public void markCompleted(Long appointmentId) {
        AppointmentReminder reminder = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("there is no such reminder in the database"));

        reminder.setCompleted(true);

        appointmentRepository.save(reminder);

    }
}
