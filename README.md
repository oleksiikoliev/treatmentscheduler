
# Treatment Scheduler (Test Task)

This project implements a **treatment scheduler** service in `com.example.treatmentscheduler.service.TreatmentTaskScheduler`. The service is responsible for generating and saving treatment tasks based on treatment plans stored in the database. The logic ensures that each task follows the plan’s defined schedule, and it handles exceptions gracefully during task creation.

---

## **Service Logic**

The service logic is implemented in the `TreatmentTaskScheduler` class. Here’s the core functionality:

1. **Fetch Treatment Plans**:  
   The service retrieves all treatment plans from the database using `TreatmentPlanRepository`.

2. **Generate Treatment Tasks**:  
   For every treatment plan, the service generates tasks for each day within the range defined by the plan's start and end date.

3. **Task Persistence**:  
   Each generated task is persisted to the database using `TreatmentTaskRepository`.

4. **Validation and Exception Handling**:
    - If a **plan is missing a required patient reference**, a `TreatmentSchedulerException` is thrown.
    - If a task fails to save, the exception is logged, and a `TreatmentSchedulerException` is raised with relevant details.

5. **Endless Plans Support**:
    - If a plan has no end date, tasks are generated **up to one year** from the start date.

---

## **Database Tables**

Two tables are used to store treatment-related data:

1. **`treatment_plan`**:  
   Stores the details of the treatment plan (e.g., action type, patient reference, start and end date).

2. **`treatment_task`**:  
   Stores individual treatment tasks linked to a plan, including the task’s status and start time.

---

## **JPA Entities and Repositories**

The following entities and repositories are implemented to support the service logic:

1. **Entities**:
    - `TreatmentPlan`: Represents a treatment plan with details about the schedule and patient.
    - `TreatmentTask`: Represents individual tasks generated from a treatment plan.

2. **Repositories**:
    - `TreatmentPlanRepository`: Manages CRUD operations for treatment plans.
    - `TreatmentTaskRepository`: Manages CRUD operations for treatment tasks.

---

## **Test Implementation**

The service logic is validated in the `TreatmentTaskSchedulerTest` class:

- **Test Scenarios**:
    1. **Valid Plan Execution**: Confirms that tasks are generated and saved for each day in the plan’s date range.
    2. **Task Save Failure Handling**: Ensures that a `TreatmentSchedulerException` is thrown when saving a task fails.
    3. **Handling Plans with Missing Patient Reference**: Verifies that a `TreatmentSchedulerException` is raised if a patient reference is not provided.

---

## **How to Run the Project**

1. **Prerequisites**:
    - Java 17
    - Maven or Gradle
    - H2 in-memory database (configured for testing)

2. **Build and Run the Project**:  
   Use Maven or Gradle to build and run the project:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Running Tests**:  
   Execute the tests to validate the logic:

   ```bash
   mvn test
   ```

---

## **Summary**

The `TreatmentTaskScheduler` service ensures that treatment tasks are generated based on the defined treatment plans. The service handles all key scenarios such as:
- Generating tasks within the plan’s date range.
- Handling endless plans by limiting task generation to one year.
- Managing save failures with custom exception handling.

This project demonstrates robust task scheduling and persistence using **Spring Boot** and **JPA**.
