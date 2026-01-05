package com.healthcare.personal_health_monitoring.repository;

import com.healthcare.personal_health_monitoring.entity.IdSequence;
import com.healthcare.personal_health_monitoring.entity.SequenceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdSequenceRepository extends JpaRepository<IdSequence, SequenceType> {
}
