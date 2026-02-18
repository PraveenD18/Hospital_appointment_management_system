package com.ey.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.model.Appointment;
import com.ey.model.Doctor;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	Optional<Appointment> findByDoctorAndAppointmentDateTime(
            Doctor doctor, LocalDateTime appointmentDateTime);

    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    List<Appointment> findByPatient_PatientId(Long patientId);


}
