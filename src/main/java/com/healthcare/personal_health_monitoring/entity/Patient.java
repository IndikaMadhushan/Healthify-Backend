package com.healthcare.personal_health_monitoring.entity;

import com.healthcare.personal_health_monitoring.entity.converter.EncryptedStringConverter;
import com.healthcare.personal_health_monitoring.util.SensitiveDataSupport;
import jakarta.persistence.*;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SecondaryRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
@SecondaryRow(table = "patient_personal_details", optional = false)
@SecondaryRow(table = "patient_address_details", optional = false)
@SecondaryRow(table = "patient_family_details", optional = false)
@SecondaryTables({
        @SecondaryTable(name = "patient_personal_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "patient_id")),
        @SecondaryTable(name = "patient_address_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "patient_id")),
        @SecondaryTable(name = "patient_family_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "patient_id"))
})
public class Patient {

    @Id
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", table = "patient_personal_details", nullable = false)),
            @AttributeOverride(name = "secondName", column = @Column(name = "second_name", table = "patient_personal_details")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", table = "patient_personal_details"))
    })
    private PersonName name = new PersonName();

        @Convert(converter = EncryptedStringConverter.class)
        @Column(name = "full_name", nullable = false, length = 512)
        private String fullName;

    @Column(name = "patient_code", unique = true, nullable = false)
    private String patientId;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_personal_details")
    private String nic;

    @Column(name = "nic_hash", table = "patient_personal_details", length = 64)
    private String nicHash;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_address_details")
    private String postalCode;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_personal_details")
    private String phone;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_personal_details")
    private String maritalStatus;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_personal_details")
    private String occupation;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_address_details")
    private String district;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_address_details", length = 1024)
    private String address;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(nullable = true, table = "patient_personal_details")
    private String nationality;

    @Column(nullable = false, table = "patient_personal_details")
    private LocalDate dateOfBirth;

    @Column(name = "age", nullable = false, table = "patient_personal_details")
    private Integer age;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(table = "patient_personal_details")
    private String gender;

    @Column(table = "patient_personal_details")
    private Double height;

    @Column(table = "patient_personal_details")
    private Double weight;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(table = "patient_personal_details")
    private String bloodType;

    @Column(table = "patient_personal_details", length = 1000)
    private String photoUrl;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "father_name", table = "patient_family_details")),
            @AttributeOverride(name = "dob", column = @Column(name = "father_dob", table = "patient_family_details")),
            @AttributeOverride(name = "alive", column = @Column(name = "father_alive", table = "patient_family_details")),
            @AttributeOverride(name = "causeOfDeath", column = @Column(name = "father_cause_of_death", table = "patient_family_details")),
            @AttributeOverride(name = "diseases", column = @Column(name = "father_diseases", table = "patient_family_details"))
    })
    private FamilyMember father;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "mother_name", table = "patient_family_details")),
            @AttributeOverride(name = "dob", column = @Column(name = "mother_dob", table = "patient_family_details")),
            @AttributeOverride(name = "alive", column = @Column(name = "mother_alive", table = "patient_family_details")),
            @AttributeOverride(name = "causeOfDeath", column = @Column(name = "mother_cause_of_death", table = "patient_family_details")),
            @AttributeOverride(name = "diseases", column = @Column(name = "mother_diseases", table = "patient_family_details"))
    })
    private FamilyMember mother;

    @ElementCollection
    @CollectionTable(name = "siblings", joinColumns = @JoinColumn(name = "patient_id"))
    private List<FamilyMember> siblings;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private PatientEmergencyContact emergencyContact;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientDisease> diseases;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientAllergy> allergies;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Surgery> surgeries;

    private LocalDateTime updatedAt;

    public void setEmail(String email) {
        if (user == null) {
            user = new User();
        }
        user.setEmail(email);
    }

    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }

    @Transient
    public String getFullName() {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        return name != null ? name.getFullName() : null;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        if (name == null) {
            name = new PersonName();
        }
        name.setFullName(fullName);
    }

    @Transient
    public String getFirstName() {
        return name != null ? name.getFirstName() : null;
    }

    public void setFirstName(String firstName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setFirstName(firstName);
    }

    @Transient
    public String getSecondName() {
        return name != null ? name.getSecondName() : null;
    }

    public void setSecondName(String secondName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setSecondName(secondName);
    }

    @Transient
    public String getLastName() {
        return name != null ? name.getLastName() : null;
    }

    public void setLastName(String lastName) {
        if (name == null) {
            name = new PersonName();
        }
        name.setLastName(lastName);
    }

    public void setNic(String nic) {
        this.nic = nic;
        this.nicHash = SensitiveDataSupport.blindIndex(nic);
    }

    public void setEmergencyContact(PatientEmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
        if (emergencyContact != null) {
            emergencyContact.setPatient(this);
        }
    }

    @Transient
    public EmergencyContact getPrimaryContact() {
        return emergencyContact != null ? emergencyContact.getPrimaryContact() : null;
    }

    public void setPrimaryContact(EmergencyContact primaryContact) {
        if (primaryContact == null && emergencyContact == null) {
            return;
        }
        ensureEmergencyContact().setPrimaryContact(primaryContact);
    }

    @Transient
    public EmergencyContact getSecondaryContact() {
        return emergencyContact != null ? emergencyContact.getSecondaryContact() : null;
    }

    public void setSecondaryContact(EmergencyContact secondaryContact) {
        if (secondaryContact == null && emergencyContact == null) {
            return;
        }
        ensureEmergencyContact().setSecondaryContact(secondaryContact);
    }

    private PatientEmergencyContact ensureEmergencyContact() {
        if (emergencyContact == null) {
            emergencyContact = new PatientEmergencyContact();
            emergencyContact.setPatient(this);
        }
        return emergencyContact;
    }
}
