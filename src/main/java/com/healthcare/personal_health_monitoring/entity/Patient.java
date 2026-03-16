package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
@SecondaryTables({
        @SecondaryTable(name = "patient_personal_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "patient_id")),
        @SecondaryTable(name = "patient_address_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "patient_id")),
        @SecondaryTable(name = "patient_family_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "patient_id")),
        @SecondaryTable(name = "patient_emergency_contacts",
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

    @Column(name = "patient_code", unique = true, nullable = false)
    private String patientId;

    @Column(nullable = true, table = "patient_personal_details")
    private String nic;

    @Column(nullable = true, table = "patient_address_details")
    private String postalCode;

    @Column(nullable = true, table = "patient_personal_details")
    private String phone;

    @Column(nullable = true, table = "patient_personal_details")
    private String maritalStatus;

    @Column(nullable = true, table = "patient_personal_details")
    private String occupation;

    @Column(nullable = true, table = "patient_address_details")
    private String district;

    @Column(nullable = true, table = "patient_address_details")
    private String address;

    @Column(nullable = true, table = "patient_personal_details")
    private String nationality;

    @Column(nullable = false, table = "patient_personal_details")
    private LocalDate dateOfBirth;

    @Column(name = "age", nullable = false, table = "patient_personal_details")
    private Integer age;

    @Column(table = "patient_personal_details")
    private String gender;

    @Column(table = "patient_personal_details")
    private Double height;

    @Column(table = "patient_personal_details")
    private Double weight;

    @Column(table = "patient_personal_details")
    private String bloodType;

    @Column(table = "patient_personal_details")
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "primary_contact_name", table = "patient_emergency_contacts")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "primary_contact_phone", table = "patient_emergency_contacts")),
            @AttributeOverride(name = "relationship", column = @Column(name = "primary_contact_relationship", table = "patient_emergency_contacts"))
    })
    private EmergencyContact primaryContact;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "secondary_contact_name", table = "patient_emergency_contacts")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "secondary_contact_phone", table = "patient_emergency_contacts")),
            @AttributeOverride(name = "relationship", column = @Column(name = "secondary_contact_relationship", table = "patient_emergency_contacts"))
    })
    private EmergencyContact secondaryContact;

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
        return name != null ? name.getFullName() : null;
    }

    public void setFullName(String fullName) {
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
}
