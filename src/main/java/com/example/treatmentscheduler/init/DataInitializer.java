package com.example.treatmentscheduler.init;

import com.example.treatmentscheduler.entity.TreatmentPlan;
import com.example.treatmentscheduler.repo.TreatmentPlanRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.treatmentscheduler.enums.TreatmentAction;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final TreatmentPlanRepository treatmentPlanRepository;

    public DataInitializer(TreatmentPlanRepository treatmentPlanRepository) {
        this.treatmentPlanRepository = treatmentPlanRepository;
    }

    @Override
    public void run(String... args) {
        // Prepare sample treatment plans
        TreatmentPlan plan1 = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_A)
                .patientReference("Patient A")
                .startTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(3).withHour(8).withMinute(0))
                .recurrencePattern("daily at 08:00")
                .build();

        TreatmentPlan plan2 = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_B)
                .patientReference("Patient B")
                .startTime(LocalDateTime.now().plusDays(2).withHour(8).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(5).withHour(8).withMinute(0))
                .recurrencePattern("every day at 12:00")
                .build();

        // Save to the repository
        treatmentPlanRepository.saveAll(List.of(plan1, plan2));

        System.out.println("Sample treatment plans inserted.");
    }
}