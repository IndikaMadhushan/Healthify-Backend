package com.healthcare.personal_health_monitoring.entity;

import com.healthcare.personal_health_monitoring.util.NameUtil;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PersonName {

    private String firstName;
    private String secondName;
    private String lastName;

    @Transient
    public String getFullName() {
        return NameUtil.combine(firstName, secondName, lastName);
    }

    public void setFullName(String fullName) {
        NameUtil.NameParts parts = NameUtil.split(fullName);
        this.firstName = parts.firstName();
        this.secondName = parts.secondName();
        this.lastName = parts.lastName();
    }
}
