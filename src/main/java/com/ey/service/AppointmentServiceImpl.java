package com.ey.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.request.AppointmentRequest;
import com.ey.dto.response.AppointmentResponse;
import com.ey.enums.AppointmentStatus;
import com.ey.enums.AppointmentType;
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

        appointmentRepository.findByDoctorAndAppointmentDateTime(doctor, request.getAppointmentDateTime())
                .ifPresent(a -> { throw new SlotAlreadyBookedException("Slot already booked"); });

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
                .collect(Collectors.toList()); // use Collectors.toList() if you're on Java 8+
    }

    // --- NEW: search ---

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> searchAppointments(LocalDateTime start,
                                                        LocalDateTime end,
                                                        Long doctorId,
                                                        Long patientId,
                                                        AppointmentStatus status,
                                                        AppointmentType type) {

        List<Appointment> appointments = appointmentRepository.findAll(); // simple fetch

        return appointments.stream()
                .filter(a -> {
                    // start date filter
                    if (start != null && a.getAppointmentDateTime().isBefore(start)) {
                        return false;
                    }

                    // end date filter
                    if (end != null && a.getAppointmentDateTime().isAfter(end)) {
                        return false;
                    }

                    // doctor filter
                    if (doctorId != null && (a.getDoctor() == null ||
                            !a.getDoctor().getDoctorId().equals(doctorId))) {
                        return false;
                    }

                    // patient filter
                    if (patientId != null && (a.getPatient() == null ||
                            !a.getPatient().getPatientId().equals(patientId))) {
                        return false;
                    }

                    // status filter
                    if (status != null && a.getStatus() != status) {
                        return false;
                    }

                    // type filter
                    if (type != null && a.getType() != type) {
                        return false;
                    }

                    return true;
                })
                .map(appointmentMapper::toResponse)  // use your existing mapper
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(appointmentMapper::toResponse)      // <-- use mapper
                .collect(Collectors.toList());
    }

    // ===== Inline Specifications =====

    private Specification<Appointment> hasDoctor(Long doctorId) {
        // If your Doctor PK is "id", change "doctorId" -> "id"
        return (root, query, cb) -> cb.equal(root.get("doctor").get("doctorId"), doctorId);
    }

    private Specification<Appointment> hasPatient(Long patientId) {
        // If your Patient PK is "id", change "patientId" -> "id"
        return (root, query, cb) -> cb.equal(root.get("patient").get("patientId"), patientId);
    }

    private Specification<Appointment> hasStatus(AppointmentStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private Specification<Appointment> hasType(AppointmentType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    private Specification<Appointment> between(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> cb.between(root.get("appointmentDateTime"), start, end);
    }

    private Specification<Appointment> startsOnOrAfter(LocalDateTime start) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("appointmentDateTime"), start);
    }

    private Specification<Appointment> endsOnOrBefore(LocalDateTime end) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("appointmentDateTime"), end);
    }
}
