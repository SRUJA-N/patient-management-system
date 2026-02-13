package com.patientmanagement.demo.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotNull
    private String phoneNumber;

    @Min(1)
    @Max(3)
    private int priority; 

    @NotNull
    private LocalDate dateOfBirth;
}