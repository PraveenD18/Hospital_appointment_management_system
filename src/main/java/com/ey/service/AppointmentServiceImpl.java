package com.ey.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ey.dto.request.AppointmentRequest;
import com.ey.dto.response.AppointmentResponse;
import com.ey.enums.AppointmentStatus;
import com.ey.exception.ResourceNotFoundException;
import com.ey.exception.SlotAlreadyBookedException;
import com.ey.mapper.AppointmentMapper;
import com.ey.model.Appointment;
import com.ey.model.Doctor;
import com.ey.model.Patient;
import com.ey.repository.AppointmentRepository;
import com.ey.repository.DoctorRepository;
import com.ey.repository.PatientRepository;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  DoctorRepository doctorRepository,
                                  PatientRepository patientRepository,
                                  AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        appointmentRepository.findByDoctorAndAppointmentDateTime(
                doctor, request.getAppointmentDateTime()
        ).ifPresent(a -> { throw new SlotAlreadyBookedException("Slot already booked"); });

        Appointment appointment = appointmentMapper.toEntity(request, doctor, patient);
        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointmentMapper.updateStatus(appointment, AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId)
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {

        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setStatus(status);

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(saved);
    }

	@Override
	public List<AppointmentResponse> getAllAppointments() {
	    return appointmentRepository.findAll()
	            .stream()
	            .map(appointmentMapper::toResponse)
	            .toList();
	}

}
