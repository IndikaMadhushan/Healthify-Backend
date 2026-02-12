package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import com.healthcare.personal_health_monitoring.entity.ConsultPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ConsultRepo extends JpaRepository<ConsultPage,Integer>{
    List<ConsultPage> findAllByPatient_Id(long patientId);
    List<ConsultPage> findByPatient_id(Long id);

}



