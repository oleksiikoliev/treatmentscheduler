package com.example.treatmentscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TreatmentSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreatmentSchedulerApplication.class, args);
    }

}
