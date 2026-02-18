package com.ey.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ey.enums.Specialization;
import com.ey.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialization(Specialization specialization);
}