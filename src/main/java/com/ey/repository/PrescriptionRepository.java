package com.ey.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.enums.PrescriptionStatus;
import com.ey.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findByAppointment_AppointmentId(Long appointmentId);

    List<Prescription> findByStatus(PrescriptionStatus status);
}