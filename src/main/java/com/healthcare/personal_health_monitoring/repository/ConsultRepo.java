package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import com.healthcare.personal_health_monitoring.entity.ConsultPage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ConsultRepo extends JpaRepository<ConsultPage,Integer>{
    @EntityGraph(attributePaths = {"doctor", "doctor.user"})
    List<ConsultPage> findAllByPatient_Id(long patientId);
    List<ConsultPage> findByPatient_id(Long id);

    @EntityGraph(attributePaths = {"doctor", "doctor.user", "patient", "patient.user"})
    Optional<ConsultPage> findOneByConsultId(int consultId);

}



