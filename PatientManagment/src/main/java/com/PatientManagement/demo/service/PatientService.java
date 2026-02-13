package com.PatientManagement.demo.service;

import org.springframework.stereotype.Service;

import com.PatientManagement.demo.repository.PatientRepository;


@Service
public class PatientService {
    PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository){
        this.patientRepository= patientRepository;
    }
    
}
