//package com.healthcare.personal_health_monitoring.repository;
//
//import com.healthcare.personal_health_monitoring.entity.MedicineReminder;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface MedicineReminderRepository extends JpaRepository<MedicineReminder, Long> {
//    List<MedicineReminder> findByPatientIdAndActiveTrue(Long patientId);
//    List<MedicineReminder> findByActiveTrue();
//}
