package com.patientmanagement.demo.dto;

import lombok.Data;

@Data
public class PatientResponseDTO {
    private String name;
    private String id;
    private String phoneNumber;
    private String dateOfBirth;
    private String registerDate;
    private int priority;
    private String email;
}
