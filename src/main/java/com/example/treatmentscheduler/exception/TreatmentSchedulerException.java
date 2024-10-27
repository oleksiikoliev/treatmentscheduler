package com.example.treatmentscheduler.exception;

public class TreatmentSchedulerException extends RuntimeException {

    private static final long serialVersionUID = 872634872364982374L;
    public TreatmentSchedulerException(String message) {
        super(message);
    }

    public TreatmentSchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
}
