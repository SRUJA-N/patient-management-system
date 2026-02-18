package com.patientmanagement.demo.mapper;

import com.patientmanagement.demo.dto.PatientRequestDTO;
import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.model.Patient;

public class PatientMapper {
    public static PatientResponseDTO toDto(Patient patient){
    PatientResponseDTO patientDto=new PatientResponseDTO();
    patientDto.setId(patient.getId().toString());
    patientDto.setName(patient.getName());
    patientDto.setEmail(patient.getEmail());
    patientDto.setPhoneNumber(patient.getPhoneNumber());
    patientDto.setDateOfBirth(patient.getDateOfBirth().toString());
    patientDto.setRegisterDate(patient.getRegisterDate().toString());
    patientDto.setPriority(patient.getPriority());
    return patientDto;
    
    }
    
    public static Patient toModel(PatientRequestDTO dto){
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setPriority(dto.getPriority());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setRegisterDate(dto.getRegisterDate());
        return patient;
    }
    
}
