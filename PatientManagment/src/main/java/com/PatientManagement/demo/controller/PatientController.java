package com.patientmanagement.demo.controller;
import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.service.PatientService;

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




}