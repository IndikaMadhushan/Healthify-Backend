package com.healthcare.personal_health_monitoring.config;

import com.healthcare.personal_health_monitoring.util.NameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LegacyProfileMigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        migratePatientData();
        migrateDoctorData();
        migratePendingRegistrationNames();
    }

    private void migratePatientData() {
        if (!columnExists("patients", "full_name")) {
            return;
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                SELECT id, full_name, nic, phone, marital_status, occupation, nationality,
                       date_of_birth, age, gender, height, weight, blood_type, photo_url,
                       postal_code, district, address,
                       father_name, father_dob, father_alive, father_cause_of_death, father_diseases,
                       mother_name, mother_dob, mother_alive, mother_cause_of_death, mother_diseases,
                       primary_contact_name, primary_contact_phone, primary_contact_relationship,
                       secondary_contact_name, secondary_contact_phone, secondary_contact_relationship
                FROM patients
                """);

        for (Map<String, Object> row : rows) {
            long patientId = asLong(row.get("id"));
            NameUtil.NameParts parts = NameUtil.split((String) row.get("full_name"));

            Integer age = resolveAge(row.get("age"), row.get("date_of_birth"));

            jdbcTemplate.update("""
                            INSERT INTO patient_personal_details
                                (patient_id, first_name, second_name, last_name, nic, phone, marital_status,
                                 occupation, nationality, date_of_birth, age, gender, height, weight,
                                 blood_type, photo_url)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE
                                first_name = VALUES(first_name),
                                second_name = VALUES(second_name),
                                last_name = VALUES(last_name),
                                nic = VALUES(nic),
                                phone = VALUES(phone),
                                marital_status = VALUES(marital_status),
                                occupation = VALUES(occupation),
                                nationality = VALUES(nationality),
                                date_of_birth = VALUES(date_of_birth),
                                age = VALUES(age),
                                gender = VALUES(gender),
                                height = VALUES(height),
                                weight = VALUES(weight),
                                blood_type = VALUES(blood_type),
                                photo_url = VALUES(photo_url)
                            """,
                    patientId,
                    parts.firstName(),
                    parts.secondName(),
                    parts.lastName(),
                    row.get("nic"),
                    row.get("phone"),
                    row.get("marital_status"),
                    row.get("occupation"),
                    row.get("nationality"),
                    row.get("date_of_birth"),
                        age,
                    row.get("gender"),
                    row.get("height"),
                    row.get("weight"),
                    row.get("blood_type"),
                    row.get("photo_url")
            );

            jdbcTemplate.update("""
                            INSERT INTO patient_address_details
                                (patient_id, postal_code, district, address)
                            VALUES (?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE
                                postal_code = VALUES(postal_code),
                                district = VALUES(district),
                                address = VALUES(address)
                            """,
                    patientId,
                    row.get("postal_code"),
                    row.get("district"),
                    row.get("address")
            );

            jdbcTemplate.update("""
                            INSERT INTO patient_family_details
                                (patient_id, father_name, father_dob, father_alive, father_cause_of_death, father_diseases,
                                 mother_name, mother_dob, mother_alive, mother_cause_of_death, mother_diseases)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE
                                father_name = VALUES(father_name),
                                father_dob = VALUES(father_dob),
                                father_alive = VALUES(father_alive),
                                father_cause_of_death = VALUES(father_cause_of_death),
                                father_diseases = VALUES(father_diseases),
                                mother_name = VALUES(mother_name),
                                mother_dob = VALUES(mother_dob),
                                mother_alive = VALUES(mother_alive),
                                mother_cause_of_death = VALUES(mother_cause_of_death),
                                mother_diseases = VALUES(mother_diseases)
                            """,
                    patientId,
                    row.get("father_name"),
                    row.get("father_dob"),
                    row.get("father_alive"),
                    row.get("father_cause_of_death"),
                    row.get("father_diseases"),
                    row.get("mother_name"),
                    row.get("mother_dob"),
                    row.get("mother_alive"),
                    row.get("mother_cause_of_death"),
                    row.get("mother_diseases")
            );

            jdbcTemplate.update("""
                            INSERT INTO patient_emergency_contacts
                                (patient_id, primary_contact_name, primary_contact_phone, primary_contact_relationship,
                                 secondary_contact_name, secondary_contact_phone, secondary_contact_relationship)
                            VALUES (?, ?, ?, ?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE
                                primary_contact_name = VALUES(primary_contact_name),
                                primary_contact_phone = VALUES(primary_contact_phone),
                                primary_contact_relationship = VALUES(primary_contact_relationship),
                                secondary_contact_name = VALUES(secondary_contact_name),
                                secondary_contact_phone = VALUES(secondary_contact_phone),
                                secondary_contact_relationship = VALUES(secondary_contact_relationship)
                            """,
                    patientId,
                    row.get("primary_contact_name"),
                    row.get("primary_contact_phone"),
                    row.get("primary_contact_relationship"),
                    row.get("secondary_contact_name"),
                    row.get("secondary_contact_phone"),
                    row.get("secondary_contact_relationship")
            );
        }

        log.info("Migrated {} legacy patient records into normalized tables", rows.size());
    }

    private void migrateDoctorData() {
        if (!columnExists("doctors", "full_name")) {
            return;
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                SELECT id, full_name, gender, hospital, nic, postal_code, verification_doc_url,
                       phone, district, province, country, specialization, license_number,
                       date_of_birth, age, photo_url, joined_date
                FROM doctors
                """);

        for (Map<String, Object> row : rows) {
            long doctorId = asLong(row.get("id"));
            NameUtil.NameParts parts = NameUtil.split((String) row.get("full_name"));

            Integer age = resolveAge(row.get("age"), row.get("date_of_birth"));

            jdbcTemplate.update("""
                            INSERT INTO doctor_personal_details
                                (doctor_id, first_name, second_name, last_name, gender, nic, postal_code,
                                 phone, district, province, country, date_of_birth, age, photo_url)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE
                                first_name = VALUES(first_name),
                                second_name = VALUES(second_name),
                                last_name = VALUES(last_name),
                                gender = VALUES(gender),
                                nic = VALUES(nic),
                                postal_code = VALUES(postal_code),
                                phone = VALUES(phone),
                                district = VALUES(district),
                                province = VALUES(province),
                                country = VALUES(country),
                                date_of_birth = VALUES(date_of_birth),
                                age = VALUES(age),
                                photo_url = VALUES(photo_url)
                            """,
                    doctorId,
                    parts.firstName(),
                    parts.secondName(),
                    parts.lastName(),
                    row.get("gender"),
                    row.get("nic"),
                    row.get("postal_code"),
                    row.get("phone"),
                    row.get("district"),
                    row.get("province"),
                    row.get("country"),
                    row.get("date_of_birth"),
                        age,
                    row.get("photo_url")
            );

                String licenseNumber = resolveNonEmptyString(
                    row.get("license_number"),
                    "UNKNOWN-" + doctorId
                );

                jdbcTemplate.update("""
                            INSERT INTO doctor_professional_details
                                (doctor_id, hospital, specialization, license_number, verification_doc_url, joined_date)
                            VALUES (?, ?, ?, ?, ?, ?)
                            ON DUPLICATE KEY UPDATE
                                hospital = VALUES(hospital),
                                specialization = VALUES(specialization),
                                license_number = VALUES(license_number),
                                verification_doc_url = VALUES(verification_doc_url),
                                joined_date = VALUES(joined_date)
                            """,
                    doctorId,
                    row.get("hospital"),
                    row.get("specialization"),
                    licenseNumber,
                    row.get("verification_doc_url"),
                    row.get("joined_date")
            );
        }

        log.info("Migrated {} legacy doctor records into normalized tables", rows.size());
    }

    private void migratePendingRegistrationNames() {
        if (!columnExists("pending_registrations", "full_name")) {
            return;
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                SELECT id, full_name, first_name, second_name, last_name
                FROM pending_registrations
                """);

        for (Map<String, Object> row : rows) {
            String firstName = (String) row.get("first_name");
            String lastName = (String) row.get("last_name");
            if (firstName != null && !firstName.isBlank() && lastName != null && !lastName.isBlank()) {
                continue;
            }

            NameUtil.NameParts parts = NameUtil.split((String) row.get("full_name"));
            jdbcTemplate.update("""
                            UPDATE pending_registrations
                            SET first_name = ?, second_name = ?, last_name = ?
                            WHERE id = ?
                            """,
                    parts.firstName(),
                    parts.secondName(),
                    parts.lastName(),
                    row.get("id")
            );
        }

        log.info("Migrated legacy pending registration names");
    }

    private boolean columnExists(String tableName, String columnName) {
        Integer matches = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = ?
                  AND COLUMN_NAME = ?
                """, Integer.class, tableName, columnName);
        return matches != null && matches > 0;
    }

    private long asLong(Object value) {
        return value instanceof Number number ? number.longValue() : Long.parseLong(String.valueOf(value));
    }

    private Integer resolveAge(Object ageValue, Object dobValue) {
        if (ageValue instanceof Number number) {
            return number.intValue();
        }

        LocalDate dob = asLocalDate(dobValue);
        if (dob != null) {
            return Period.between(dob, LocalDate.now()).getYears();
        }

        return 0;
    }

    private LocalDate asLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime().toLocalDate();
        }
        return LocalDate.parse(String.valueOf(value));
    }

    private String resolveNonEmptyString(Object value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? fallback : text;
    }
}
