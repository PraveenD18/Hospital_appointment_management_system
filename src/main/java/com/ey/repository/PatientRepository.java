package com.ey.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long>{

}
