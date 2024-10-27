package com.example.treatmentscheduler.entity;

import com.example.treatmentscheduler.enums.TaskStatus;
import com.example.treatmentscheduler.enums.TreatmentAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "treatment_task")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class TreatmentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "treatment_action", nullable = false)
    @Enumerated(EnumType.STRING)
    private TreatmentAction action;

    @Column(name = "patient", nullable = false)
    private String patientReference;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_plan_id", nullable = false)
    private TreatmentPlan treatmentPlan;
}
