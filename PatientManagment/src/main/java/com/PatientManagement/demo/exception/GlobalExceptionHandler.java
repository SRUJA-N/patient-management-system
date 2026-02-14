package com.patientmanagement.demo.exception;

import org.hibernate.engine.jdbc.env.internal.LobCreationLogging_.logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<Map<String,String>> handleEmailAlreadyExistException(EmailAlreadyExistException ex){
        log.warn("email address alreaady exisy{}",ex.getMesssage());
        Map<String,String> errors=new HashMap<>();
        errors.put("message","emial adress already exist");
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(PatientNotFoundException.class)
     public ResponseEntity<Map<String,String>> handlePatientNotFoundException(PatientNotFoundException ex){
        Map<String,String> errors=new HashMap<>();
        errors.put("message","patient not found");
          return ResponseEntity.badRequest().body(errors);
     }
}