package com.healthcare.personal_health_monitoring.entity;

import com.healthcare.personal_health_monitoring.entity.converter.EncryptedStringConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class EmergencyContact {
    @Convert(converter = EncryptedStringConverter.class)
    private String name;

    @Convert(converter = EncryptedStringConverter.class)
    private String phoneNumber;

    @Convert(converter = EncryptedStringConverter.class)
    private String relationship;
}
