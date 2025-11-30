package com.healthcare.personal_health_monitoring.service;

import com.healthcare.personal_health_monitoring.dto.AllergyDTO;
import java.util.List;

public interface AllergyService {
    AllergyDTO create(String name);
    List<AllergyDTO> listAll();
}
