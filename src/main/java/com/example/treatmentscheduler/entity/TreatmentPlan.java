package com.example.treatmentscheduler.entity;

import com.example.treatmentscheduler.enums.TreatmentAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "treatment_plan")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class TreatmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private TreatmentAction action;

    @Column(name = "patient", nullable = false)
    private String patientReference;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "recurrence_pattern", nullable = false)
    private String recurrencePattern; // "every day at 08:00 and 16:00" or another example, “every Monday at 12:00”

    @OneToMany(mappedBy = "treatmentPlan", cascade = REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TreatmentTask> tasks;
}
