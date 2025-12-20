package com.healthcare.personal_health_monitoring.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity

public class Admin  {
    @Id
    private long id;

    @Getter
    @OneToOne
    @MapsId //tells Hibernate to use the User's ID as the Patient's ID
    @JoinColumn(name = "id")
    private User user;

    public void setPassword(String password) {
        user.setPassword(password);
    }


}
