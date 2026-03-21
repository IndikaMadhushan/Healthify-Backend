package com.healthcare.personal_health_monitoring.entity.converter;

import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return SensitiveDataSupport.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return SensitiveDataSupport.decrypt(dbData);
    }
}
