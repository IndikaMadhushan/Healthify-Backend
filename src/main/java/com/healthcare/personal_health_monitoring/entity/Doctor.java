package com.healthcare.personal_health_monitoring.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor  {

    @Id
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId //tells Hibernate to use the User's ID as the Patient's ID
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable=false)
    private String fullName;

    //generated doctor id
    @Column(name = "doctor_code", unique = true, nullable = false)
    private String doctorId;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String hospital;

    @Column(nullable=true)
    private String nic;

    @Column(nullable=true)
    private String postalCode;

    @Column(name = "verification_doc_url")
    private String verificationDocUrl;


    @Column(nullable=true)
    private String phone;

    @Column(nullable=true)
    private String district;

    @Column(nullable=true)
    private String province;

    @Column(nullable=true)
    private String country;

    private String specialization;

    @Column(unique = true, nullable = false)
    private String licenseNumber;

    private LocalDate dateOfBirth;
    @Column(name = "age")
    private Integer age;

    private String photoUrl;

    private LocalDateTime joinedDate = LocalDateTime.now();

    public int getExperience() {

        return LocalDateTime.now().getYear() - joinedDate.getYear();
    }

    //private String email = user.getEmail();

    // this is for data initializer
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
}