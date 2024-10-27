package com.example.treatmentscheduler.service;

import com.example.treatmentscheduler.entity.TreatmentPlan;
import com.example.treatmentscheduler.entity.TreatmentTask;
import com.example.treatmentscheduler.enums.TaskStatus;
import com.example.treatmentscheduler.enums.TreatmentAction;
import com.example.treatmentscheduler.exception.TreatmentSchedulerException;
import com.example.treatmentscheduler.repo.TreatmentPlanRepository;
import com.example.treatmentscheduler.repo.TreatmentTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TreatmentTaskScheduler {
    private final TreatmentPlanRepository planRepository;
    private final TreatmentTaskRepository taskRepository;

    @Autowired
    public TreatmentTaskScheduler(TreatmentPlanRepository planRepository,
                                  TreatmentTaskRepository taskRepository) {
        this.planRepository = planRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * Create treatment tasks based on new plans.
     * This method processes all treatment plans from the database.
     */
    @Transactional
    public void processNewPlans() {
        List<TreatmentPlan> plans = getTreatmentPlans();
        int newTasksAmount = plans.stream()
                .mapToInt(plan -> createTasksForPlan(plan).size())
                .sum();

        log.info("Successfully created {} treatment tasks for {} treatment plans.",
                newTasksAmount, plans.size());
    }

    // Create treatment tasks for a given plan
    @Transactional
    public synchronized List<TreatmentTask> createTasksForPlan(TreatmentPlan plan) {
        // Validate that the patient reference is not null
        if (plan.getPatientReference() == null) {
            String message = String.format("Invalid plan: Patient reference is required for plan %s", plan.getId());
            log.error(message);
            throw new TreatmentSchedulerException(message);
        }

        List<TreatmentTask> tasks = generateTasksFromPlan(plan);

        tasks.forEach(task -> {
            try {
                taskRepository.save(task);
                log.info("Created task {} for plan {}.", task.getId(), plan.getId());
            } catch (Exception e) {
                String message = String.format("Failed to save task for plan %s: %s",
                        plan.getId(), e.getMessage());
                log.error(message, e);
                throw new TreatmentSchedulerException(message, e);
            }
        });

        return tasks;
    }

    // Generate tasks based on the plan's recurrence pattern and schedule
    private List<TreatmentTask> generateTasksFromPlan(TreatmentPlan plan) {
        TreatmentAction action = plan.getAction();
        String patientReference = plan.getPatientReference();

        LocalDateTime start = plan.getStartTime();
        LocalDateTime end = plan.getEndTime() != null ? plan.getEndTime() : LocalDateTime.now().plusYears(1);

        List<LocalDateTime> dates = generateDates(start, end);

        return dates.stream()
                .map(date -> TreatmentTask.builder()
                        .treatmentPlan(plan)
                        .action(action)
                        .patientReference(patientReference)
                        .status(TaskStatus.ACTIVE)
                        .startTime(date)
                        .build())
                .collect(Collectors.toList());
    }

    // Fetch active treatment plans from the database
    private List<TreatmentPlan> getTreatmentPlans() {
        try {
            return planRepository.findAll();
        } catch (Exception e) {
            String message = "Failed to retrieve active treatment plans: " + e.getMessage();
            log.error(message, e);
            throw new TreatmentSchedulerException(message, e);
        }
    }

    private List<LocalDateTime> generateDates(LocalDateTime start, LocalDateTime end) {
        List<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime current = start;

        while (!current.isAfter(end)) {
            dates.add(current);
            current = current.plusDays(1);  // Adjust the interval if needed
        }

        return dates;
    }
}
