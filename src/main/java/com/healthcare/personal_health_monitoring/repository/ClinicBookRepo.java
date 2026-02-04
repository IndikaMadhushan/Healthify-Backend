package com.healthcare.personal_health_monitoring.repository;


import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ClinicBookRepo extends JpaRepository<ClinicBook,Integer> {

    List<ClinicBook> findAllByPatient_Id(long patientId);
}