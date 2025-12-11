package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient extends User {

    private LocalDate dateOfBirth;
    private String gender;
    private Double height;
    private Double weight;
    private String bloodType;
    private String postalCode;

    // Family background
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "father_name")),
            @AttributeOverride(name = "dob", column = @Column(name = "father_dob")),
            @AttributeOverride(name = "age", column = @Column(name = "father_age")),
            @AttributeOverride(name = "alive", column = @Column(name = "father_alive")),
            @AttributeOverride(name = "causeOfDeath", column = @Column(name = "father_cause_of_death")),
            @AttributeOverride(name = "diseases", column = @Column(name = "father_diseases"))
    })
    private FamilyMember father;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "mother_name")),
            @AttributeOverride(name = "dob", column = @Column(name = "mother_dob")),
            @AttributeOverride(name = "age", column = @Column(name = "mother_age")),
            @AttributeOverride(name = "alive", column = @Column(name = "mother_alive")),
            @AttributeOverride(name = "causeOfDeath", column = @Column(name = "mother_cause_of_death")),
            @AttributeOverride(name = "diseases", column = @Column(name = "mother_diseases"))
    })
    private FamilyMember mother;

    @ElementCollection
    @CollectionTable(name = "siblings", joinColumns = @JoinColumn(name = "patient_id"))
    private List<FamilyMember> siblings;

    // Emergency contacts
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "primary_contact_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "primary_contact_phone")),
            @AttributeOverride(name = "relationship", column = @Column(name = "primary_contact_relationship"))
    })
    private EmergencyContact primaryContact;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "secondary_contact_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "secondary_contact_phone")),
            @AttributeOverride(name = "relationship", column = @Column(name = "secondary_contact_relationship"))
    })
    private EmergencyContact secondaryContact;

    // Patient related entities
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientDisease> diseases;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientAllergy> allergies;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Surgery> surgeries;


    @Transient
    public Integer getAge() {
        if (dateOfBirth == null) return null;
        return LocalDate.now().getYear() - dateOfBirth.getYear();


    }
}