package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
@SecondaryTables({
        @SecondaryTable(name = "doctor_personal_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "doctor_id")),
        @SecondaryTable(name = "doctor_professional_details",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "doctor_id"))
})
public class Doctor {

    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", table = "doctor_personal_details", nullable = false)),
            @AttributeOverride(name = "secondName", column = @Column(name = "second_name", table = "doctor_personal_details")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", table = "doctor_personal_details"))
    })
    private PersonName name = new PersonName();

    @Column(name = "doctor_code", unique = true, nullable = false)
    private String doctorId;

    @Column(nullable = false, table = "doctor_personal_details")
    private String gender;

    @Column(nullable = false, table = "doctor_professional_details")
    private String hospital;

    @Column(nullable = true, table = "doctor_personal_details")
    private String nic;

    @Column(nullable = true, table = "doctor_personal_details")
    private String postalCode;

    @Column(name = "verification_doc_url", table = "doctor_professional_details", length = 1000)
    private String verificationDocUrl;

    @Column(nullable = true, table = "doctor_personal_details")
    private String phone;

    @Column(nullable = true, table = "doctor_personal_details")
    private String district;

    @Column(nullable = true, table = "doctor_personal_details")
    private String province;

    @Column(nullable = true, table = "doctor_personal_details")
    private String country;

    @Column(table = "doctor_professional_details")
    private String specialization;

    @Column(unique = true, nullable = false, table = "doctor_professional_details")
    private String licenseNumber;

    @Column(table = "doctor_personal_details")
    private LocalDate dateOfBirth;

    @Column(name = "age", table = "doctor_personal_details")
    private Integer age;

    @Column(table = "doctor_personal_details", length = 1000)
    private String photoUrl;

    @Column(table = "doctor_professional_details")
    private LocalDateTime joinedDate = LocalDateTime.now();

    public int getExperience() {
        return joinedDate == null ? 0 : LocalDateTime.now().getYear() - joinedDate.getYear();
    }

    public void setEmail(String email) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setEmail(email);
    }

    public void setPass(String password) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setPassword(password);
    }

    public void setRole(UserRole userRole) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setRole(userRole);
    }

    public void setEnabled(boolean b) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setEnabled(b);
    }

    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }

    public boolean isEnabled() {
        return user != null && user.isEnabled();
    }

    public UserRole getRole() {
        return user != null ? user.getRole() : null;
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
