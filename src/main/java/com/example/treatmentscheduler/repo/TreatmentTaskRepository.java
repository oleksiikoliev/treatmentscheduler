package com.example.treatmentscheduler.repo;

import com.example.treatmentscheduler.entity.TreatmentPlan;
import com.example.treatmentscheduler.entity.TreatmentTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreatmentTaskRepository extends JpaRepository<TreatmentTask, Long> {
    List<TreatmentTask> findByTreatmentPlan(TreatmentPlan plan);
}
