package com.ey.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ey.dto.request.PrescriptionRequest;
import com.ey.dto.request.PrescriptionUpdateRequest;
import com.ey.dto.response.PrescriptionResponse;
import com.ey.enums.PrescriptionStatus;
import com.ey.exception.ResourceNotFoundException;
import com.ey.mapper.PrescriptionMapper;
import com.ey.model.Appointment;
import com.ey.model.Prescription;
import com.ey.repository.AppointmentRepository;
import com.ey.repository.PrescriptionRepository;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository,
                                   AppointmentRepository appointmentRepository,
                                   PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    @Override
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Enforce one-to-one constraint (no duplicate prescription for the same appointment)
        prescriptionRepository.findByAppointment_AppointmentId(request.getAppointmentId())
            .ifPresent(p -> { throw new IllegalArgumentException("Prescription already exists for this appointment"); });

        Prescription entity = prescriptionMapper.toEntity(request, appointment);
        Prescription saved = prescriptionRepository.save(entity);
        return prescriptionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponse getById(Long id) {
        Prescription p = prescriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
        return prescriptionMapper.toResponse(p);
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionByAppointment(Long appointmentId) {
        Prescription p = prescriptionRepository.findByAppointment_AppointmentId(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found for appointment"));
        return prescriptionMapper.toResponse(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getByStatus(PrescriptionStatus status) {
        return prescriptionRepository.findByStatus(status)
            .stream()
            .map(prescriptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAll() {
        return prescriptionRepository.findAll()
            .stream()
            .map(prescriptionMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponse update(Long id, PrescriptionUpdateRequest request) {
        Prescription p = prescriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));

        if (request.getDiagnosis() != null) p.setDiagnosis(request.getDiagnosis());
        if (request.getMedicines() != null) p.setMedicines(request.getMedicines());
        if (request.getNotes() != null) p.setNotes(request.getNotes());
        if (request.getStatus() != null) p.setStatus(request.getStatus());

        Prescription saved = prescriptionRepository.save(p);
        return prescriptionMapper.toResponse(saved);
    }

    @Override
    public PrescriptionResponse updateStatus(Long id, PrescriptionStatus status) {
        if (status == null) throw new IllegalArgumentException("Status must not be null");
        Prescription p = prescriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
        p.setStatus(status);
        return prescriptionMapper.toResponse(prescriptionRepository.save(p));
    }

    @Override
    public void delete(Long id) {
        Prescription p = prescriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
        prescriptionRepository.delete(p);
    }
}