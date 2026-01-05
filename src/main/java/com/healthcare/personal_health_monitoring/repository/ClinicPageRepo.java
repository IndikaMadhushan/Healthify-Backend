package com.healthcare.personal_health_monitoring.repository;



import com.healthcare.personal_health_monitoring.entity.ClinicPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ClinicPageRepo extends JpaRepository<ClinicPage,Integer>
{
}
