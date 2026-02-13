package com.PatientManagement.demo.repository;

import org.hibernate.annotations.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@Repository
public interface PatientRepository extends JpaRepository<Parent,Id>{
    
    
}
