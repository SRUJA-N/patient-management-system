package com.patientmanagement.demo.service;
import java.util.*;


import org.springframework.stereotype.Service;

import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.mapper.PatientMapper;
import com.patientmanagement.demo.model.Patient;
import com.patientmanagement.demo.repository.PatientRepository;
@Service
public class PatientService{
    private final PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository){
        this.patientRepository= patientRepository;
    }
    public List<PatientResponseDTO> getAllPatient(){
        List<Patient> patients=patientRepository.findAll();
        
        List<PatientResponseDTO> patientResponseDTOs=patients.stream().map(patient ->PatientMapper.toDto(patient)).toList();
        return patientResponseDTOs;
    }
    
}
