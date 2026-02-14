package com.patientmanagement.demo.controller;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.service.PatientService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/patient")
class PatientController{
    private final PatientService patientService;
    public PatientController(PatientService patientService){
        this.patientService =patientService;
    }
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients=patientService.getAllPatient();
        return ResponseEntity.ok().body(patients);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @RequestBody PatientRequestDTO patientRequestDTO) {
        patientResponseDTO patientResponseDTO=patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }




}