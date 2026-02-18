package com.ey.service;

import com.ey.dto.request.RegisterPatientRequest;
import com.ey.dto.response.PatientResponse;

import java.util.List;

public interface PatientService {

    PatientResponse registerPatient(RegisterPatientRequest request);

    PatientResponse getPatientById(Long patientId);

    List<PatientResponse> getAllPatients();

    PatientResponse updatePatient(Long patientId, RegisterPatientRequest request);

    void deactivatePatient(Long patientId);
}