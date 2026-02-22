package com.patientmanagement.demo.service;
import java.util.*;

import org.springframework.stereotype.Service;
import com.patientmanagement.demo.dto.PatientRequestDTO;
import com.patientmanagement.demo.dto.PatientResponseDTO;
import com.patientmanagement.demo.exception.EmailAlreadyExistException;
import com.patientmanagement.demo.exception.PatientNotFoundException;
import com.patientmanagement.demo.grpc.BillingServiceClient;
import com.patientmanagement.demo.kafka.KafkaConfig;
import com.patientmanagement.demo.kafka.KafkaProducer;
import com.patientmanagement.demo.mapper.PatientMapper;
import com.patientmanagement.demo.model.Patient;
import com.patientmanagement.demo.repository.PatientRepository;

import billing.BillingServiceGrpc;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PatientService {

    private final BillingServiceClient billingServiceClient;
    private final PatientRepository patientRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PatientService(PatientRepository patientRepository, 
                          BillingServiceClient billingServiceClient, 
                          KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceClient = billingServiceClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatient() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(patient -> PatientMapper.toDto(patient))
                .toList();
        return patientResponseDTOs;
    }

    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));
        return PatientMapper.toDto(patient);
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("A patient with this email already exists: " + patientRequestDTO.getEmail());
        }
        
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        
        // Call billing service to create billing account
        billingServiceClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
        
        // Send Kafka event with patient data as JSON
        try {
            String jsonMessage = objectMapper.writeValueAsString(new HashMap<String, Object>() {{
                put("patientId", newPatient.getId().toString());
                put("name", newPatient.getName());
                put("email", newPatient.getEmail());
                put("eventType", "CREATE");
            }});
            kafkaProducer.sendMessage(KafkaConfig.PATIENT_EVENTS_TOPIC, newPatient.getId().toString(), jsonMessage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDTO dummy_data(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("A patient with this email already exists: " + patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));
        
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistException("A patient with this email already exists: " + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        patient.setPriority(patientRequestDTO.getPriority());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        patient.setRegisterDate(patientRequestDTO.getRegisterDate());
        
        Patient updatePatient = patientRepository.save(patient);
        
        // Send Kafka event for update
        try {
            String jsonMessage = objectMapper.writeValueAsString(new HashMap<String, Object>() {{
                put("patientId", patient.getId().toString());
                put("name", patient.getName());
                put("email", patient.getEmail());
                put("eventType", "UPDATE");
            }});
            kafkaProducer.sendMessage(KafkaConfig.PATIENT_EVENTS_TOPIC, patient.getId().toString(), jsonMessage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return PatientMapper.toDto(updatePatient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id " + id);
        }
        
        // Send Kafka event for delete
        try {
            String jsonMessage = objectMapper.writeValueAsString(new HashMap<String, Object>() {{
                put("patientId", id.toString());
                put("eventType", "DELETE");
            }});
            kafkaProducer.sendMessage(KafkaConfig.PATIENT_EVENTS_TOPIC, id.toString(), jsonMessage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        patientRepository.deleteById(id);
    }
}
