package com.example.treatmentscheduler.repo;

import com.example.treatmentscheduler.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {
    List<TreatmentPlan> findAllByEndTimeAfter(LocalDateTime now);
}
