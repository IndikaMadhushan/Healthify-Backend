package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient  {

    @Id
    private long id;

    @OneToOne
    @MapsId //tells Hibernate to use the User's ID as the Patient's ID
    @JoinColumn(name = "id")
    private User user;
    @Column(nullable=false)
    private String fullName;

    //for generated patient id
    @Column(name = "patient_code", unique = true, nullable = false)
    private String patientId;


    @Column(nullable=true)
    private String nic;

    @Column(nullable=true)
    private String postalCode;

    public void setEmail(String email){
        user.setEmail(email);
    }

    public String getEmail(){
        return user.getEmail();
    }


    @Column(nullable=true)
    private String phone;

    @Column(nullable = true)
    private String maritalStatus;
    @Column(nullable = true)
    private String occupation;
    @Column(nullable=true)
    private String district;
    @Column(nullable = true)
    private String address;
//    @Column(nullable=true)
//    private String province;
    @Column(nullable=true)
    private String nationality;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(name = "age")
    private Integer age;

    private String gender;
    private Double height;
    private Double weight;
    private String bloodType;



    // FAMILY BACKGROUND

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


    // EMERGENCY CONTACTS

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


    // MAPPED CHILD TABLES

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientDisease> diseases;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientAllergy> allergies;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Surgery> surgeries;



    private LocalDateTime updatedAt;
}
