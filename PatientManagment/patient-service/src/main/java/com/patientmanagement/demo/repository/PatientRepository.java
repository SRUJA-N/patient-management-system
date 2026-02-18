package com.patientmanagement.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.patientmanagement.demo.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>{
    
boolean existsByEmail(String email);
boolean existsByEmailAndIdNot(String email,Long id);

}
