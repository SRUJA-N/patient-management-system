package com.patientmanagement.demo.controller;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.patientmanagement.demo.dto.PatientRequestDTO;
import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.dto.validation.CreatePatientValidationgroup;
import com.patientmanagement.demo.service.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/patient")
@Tag(name="Patient",description ="API for patients using swagger")
public class PatientController{
    private final PatientService patientService;
    public PatientController(PatientService patientService){
        this.patientService =patientService;
    }
    @Operation(summary = "GET patients")
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients=patientService.getAllPatient();
        return ResponseEntity.ok().body(patients);
    }

   
    @GetMapping("/{id}")
      @Operation(summary = "GET patientsby id")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id){
        PatientResponseDTO patient=patientService.getPatientById(id);
        return ResponseEntity.ok().body(patient);
    }

    @PostMapping
      @Operation(summary = "Create a new patient")
   public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class,CreatePatientValidationgroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO=patientService.createPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientResponseDTO);
    }
    
    @PutMapping("/{id}")
      @Operation(summary = "update patientsby id")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @Validated({Default.class,CreatePatientValidationgroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO=patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }
    @DeleteMapping("/{id}")
      @Operation(summary = "delete patientsby id")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
patientService.deletePatient(id);
return ResponseEntity.noContent().build();
    }
    
    



}