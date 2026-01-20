package com.healthcare.personal_health_monitoring.util;

import java.time.LocalDate;
import java.time.Period;

public class AgeUtil {
    public static Integer calculateAge(LocalDate dob) {
        if(dob == null) {
            return null;
        }

        return Period.between(dob, LocalDate.now()).getYears();
    }
}
