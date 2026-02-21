package com.patientmanagement.demo.service;
import java.util.*;


import org.springframework.stereotype.Service;
import com.patientmanagement.demo.dto.PatientRequestDTO;
import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.exception.EmailAlreadyExistException;
import com.patientmanagement.demo.exception.PatientNotFoundException;
import com.patientmanagement.demo.grpc.BillingServiceClient;
import com.patientmanagement.demo.mapper.PatientMapper;
import com.patientmanagement.demo.model.Patient;
import com.patientmanagement.demo.repository.PatientRepository;
@Service
public class PatientService{

    private final BillingServiceClient billingServiceClient;
    private final PatientRepository patientRepository;
    public PatientService(PatientRepository patientRepository,BillingServiceClient billingServiceClient){
        this.patientRepository= patientRepository;
        this.billingServiceClient = billingServiceClient;
    }
    public List<PatientResponseDTO> getAllPatient(){
        List<Patient> patients=patientRepository.findAll();
        
        List<PatientResponseDTO> patientResponseDTOs=patients.stream().map(patient ->PatientMapper.toDto(patient)).toList();
        return patientResponseDTOs;
    }
    
    public PatientResponseDTO getPatientById(Long id){
        Patient patient =patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("patient not found with id "+id));
        return PatientMapper.toDto(patient);
    }
    
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
       if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("A patient with this email already exists: "+patientRequestDTO.getEmail());
        }
        Patient newPatient=patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceClient.createBillingAccount(newPatient.getId().toString(),newPatient.getName(),newPatient.getEmail());
        return PatientMapper.toDto(newPatient);
    }
     public PatientResponseDTO dummy_data(PatientRequestDTO patientRequestDTO){
       if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("A patient with this email already exists: "+patientRequestDTO.getEmail());
        }
        Patient newPatient=patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDto(newPatient);
    }
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO){

        Patient patient =patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("patient not found with id "+id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){
            throw new EmailAlreadyExistException("A patient with this email already exists: "+patientRequestDTO.getEmail());
        }
 
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        patient.setPriority(patientRequestDTO.getPriority());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        patient.setRegisterDate(patientRequestDTO.getRegisterDate());
        Patient updatePatient=patientRepository.save(patient);
        return PatientMapper.toDto(updatePatient);
    }
    public void deletePatient(Long id){
        if(!patientRepository.existsById(id)){
            throw new PatientNotFoundException("patient not found with id "+id);
        }
        patientRepository.deleteById(id);
    }
}

