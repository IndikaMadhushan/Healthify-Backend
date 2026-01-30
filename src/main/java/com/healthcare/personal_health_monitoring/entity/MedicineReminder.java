//package com.healthcare.personal_health_monitoring.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Set;
//
//@Entity
//@Table(name = "medicine_reminders" )
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class MedicineReminder {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @ManyToOne(optional = false)
//    private Patient patient;
//
//    @Column(nullable = false)
//    private String medicineName;
//    //scheduling
//    @Enumerated(EnumType.STRING)
//    private ReminderType reminderType;
//    // for specific date reminders
//    private LocalDate specificDate;
//    // whe to remind
//    private LocalTime time;
//
//    //weekly reminder
//    @ElementCollection
//    @CollectionTable(name = "reminder_days")
//    @Enumerated(EnumType.STRING)
//    private Set<DayOfWeek> daysOfWeek;
//
//    //hourly support
//    private Integer hourlyInterval;
//
//    //status
//    private boolean active = true;
//    private LocalDateTime lastTriggeredAt;
//
//}
