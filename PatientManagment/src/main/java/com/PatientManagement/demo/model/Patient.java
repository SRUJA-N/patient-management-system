package com.patientmanagement.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
@Entity
public
class Patient{
    
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    @Email
    @Column(unique=true)
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private int priority;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private LocalDate registerDate;


}