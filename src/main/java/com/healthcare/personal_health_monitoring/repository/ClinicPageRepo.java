package com.healthcare.personal_health_monitoring.repository;



import com.healthcare.personal_health_monitoring.entity.ClinicBook;
import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ClinicPageRepo extends JpaRepository<ClinicPage,Integer>
{
    List<ClinicPage> findByClinicBook_Id(int clinicBookId);
    List<ClinicPage> findByClinicBook_Patient_id(Long id);




}
