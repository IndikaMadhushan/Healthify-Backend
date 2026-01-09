package com.healthcare.personal_health_monitoring.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "id_sequences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdSequence {
    @Id
    @Enumerated(EnumType.STRING)
    private SequenceType type;

    private Long nextValue;
}
