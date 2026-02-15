package com.patientmanagement.demo.exception;

public class PatientNotFoundException extends RuntimeException{
    
    public PatientNotFoundException() {
        super();
    }
    
    public PatientNotFoundException(String message) {
        super(message);
    }
    
    public PatientNotFoundException(String message, Long id) {
        super(message + " " + id);
    }
}
