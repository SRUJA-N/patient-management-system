package com.patientmanagement.demo.exception;

public class EmailAlreadyExistException extends RuntimeException{
    
    public EmailAlreadyExistException() {
        super();
    }
    
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
