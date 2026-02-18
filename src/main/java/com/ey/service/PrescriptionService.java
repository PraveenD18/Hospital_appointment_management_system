package com.ey.service;

import java.util.List;

import com.ey.dto.request.PrescriptionRequest;
import com.ey.dto.request.PrescriptionUpdateRequest;
import com.ey.dto.response.PrescriptionResponse;
import com.ey.enums.PrescriptionStatus;

public interface PrescriptionService {
    PrescriptionResponse createPrescription(PrescriptionRequest request);
    PrescriptionResponse getById(Long id);
    PrescriptionResponse getPrescriptionByAppointment(Long appointmentId);
    List<PrescriptionResponse> getByStatus(PrescriptionStatus status);
    List<PrescriptionResponse> getAll();
    PrescriptionResponse update(Long id, PrescriptionUpdateRequest request);
    PrescriptionResponse updateStatus(Long id, PrescriptionStatus status);
    void delete(Long id);
}