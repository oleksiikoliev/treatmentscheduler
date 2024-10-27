package com.example.treatmentscheduler;

import com.example.treatmentscheduler.entity.TreatmentPlan;
import com.example.treatmentscheduler.entity.TreatmentTask;
import com.example.treatmentscheduler.enums.TaskStatus;
import com.example.treatmentscheduler.enums.TreatmentAction;
import com.example.treatmentscheduler.repo.TreatmentPlanRepository;
import com.example.treatmentscheduler.repo.TreatmentTaskRepository;
import com.example.treatmentscheduler.service.TreatmentTaskScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TreatmentTaskSchedulerTest {

    @Autowired
    private TreatmentTaskScheduler taskScheduler;

    @Autowired
    private TreatmentPlanRepository planRepository;

    @Autowired
    private TreatmentTaskRepository taskRepository;

    private TreatmentPlan futurePlan;
    private TreatmentPlan activePlan;

    @AfterEach
    void cleanUp() {
        taskRepository.deleteAll();
        planRepository.deleteAll();
    }

    @Test
    public void testFuturePlanTaskGeneration() {
        futurePlan = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_A)
                .patientReference("PatientFuture")
                .startTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(3).withHour(8).withMinute(0))
                .recurrencePattern("daily at 08:00")
                .build();
        planRepository.save(futurePlan);

        // Create a plan that starts today and is still active
        activePlan = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_B)
                .patientReference("PatientActive")
                .startTime(LocalDateTime.now().withHour(10).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(2).withHour(10).withMinute(0))
                .recurrencePattern("daily at 10:00")
                .build();
        planRepository.save(activePlan);

        // Process the future plan
        taskScheduler.processNewPlans();

        // Verify that tasks were generated for the entire future duration
        List<TreatmentTask> tasks = taskRepository.findByTreatmentPlan(futurePlan);
        assertEquals(3, tasks.size(), "Expected 3 tasks (one per day starting tomorrow).");

        // Verify task times and status
        tasks.forEach(task -> {
            assertEquals(TaskStatus.ACTIVE, task.getStatus());
            assertEquals("PatientFuture", task.getPatientReference());
            assertEquals(8, task.getStartTime().getHour());
        });
    }

    @Test
    public void testActivePlanTaskGeneration() {
        futurePlan = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_A)
                .patientReference("PatientFuture")
                .startTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(3).withHour(8).withMinute(0))
                .recurrencePattern("daily at 08:00")
                .build();
        planRepository.save(futurePlan);

        // Create a plan that starts today and is still active
        activePlan = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_B)
                .patientReference("PatientActive")
                .startTime(LocalDateTime.now().withHour(10).withMinute(0))
                .endTime(LocalDateTime.now().plusDays(4).withHour(10).withMinute(0))
                .recurrencePattern("daily at 10:00")
                .build();
        planRepository.save(activePlan);

        // Process the future plan
        taskScheduler.processNewPlans();

        // Verify that tasks were generated correctly
        List<TreatmentTask> tasks = taskRepository.findByTreatmentPlan(activePlan);
        assertEquals(5, tasks.size(), "Expected 5 tasks (one per day starting today).");

        // Verify the task details
        tasks.forEach(task -> {
            assertEquals(TaskStatus.ACTIVE, task.getStatus());
            assertEquals("PatientActive", task.getPatientReference());
            assertEquals(10, task.getStartTime().getHour());
        });
    }

    @Test
    public void testEndlessPlanTaskGeneration() {
        // Create an endless plan (no end date)
        TreatmentPlan endlessPlan = TreatmentPlan.builder()
                .action(TreatmentAction.ACTION_A)
                .patientReference("PatientEndless")
                .startTime(LocalDateTime.now().minusDays(2).withHour(9))
                .recurrencePattern("daily at 09:00")
                .build();
        planRepository.save(endlessPlan);

        // Process the plans
        taskScheduler.processNewPlans();

        // Verify tasks are generated up to today
        List<TreatmentTask> tasks = taskRepository.findByTreatmentPlan(endlessPlan);
        assertEquals(3+365, tasks.size(), "Expected 368 tasks (one per day for a one year + 3).");

        tasks.forEach(task -> {
            assertEquals(TaskStatus.ACTIVE, task.getStatus());
            assertEquals(9, task.getStartTime().getHour());
        });
    }
}
