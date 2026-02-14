package com.patientmanagement.demo.service;
import java.util.*;


import org.springframework.stereotype.Service;
import com.patientmanagement.demo.dto.PatientRequestDTO;
import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.exception.EmailAlreadyExistException;
import com.patientmanagement.demo.exception.PatientNotFoundException;
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
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("A Patient of this email is already exist"+patientRequestDTO.getEmail());
        }
        Patient newPatient=patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDto(newPatient);
    }
    public PatientResponeDTO updatePatient(Long id,patientRequestDTO patientRequestDto){

        Patient patient =patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("patient not found with id",id));
 if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("A Patient of this email is already exist"+patientRequestDTO.getEmail());
        }
        Patient newPatient=patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDto(newPatient);

    }
 
   patient.setName(patientRequestDTO.getName());
   patient.setAdress(patientRequestDTO.getAddress());
   patient.setEmail(patientRequestDTO.getEmail());
   Patient updatePatient=patientRepository.save(patient);
   return PatientMapper.toDto(updatePatient);

   


}
