package com.example.treatmentscheduler;

import com.example.treatmentscheduler.service.TreatmentTaskScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TreatmentSchedulerApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(TreatmentSchedulerApplication.class, args);

        // Retrieve the TreatmentTaskScheduler bean from the context
        TreatmentTaskScheduler scheduler = context.getBean(TreatmentTaskScheduler.class);

        // Call processNewPlans() method to schedule tasks
        scheduler.processNewPlans();
    }

}
