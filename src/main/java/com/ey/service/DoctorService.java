package com.ey.service;

import java.util.List;

import com.ey.dto.request.RegisterDoctorRequest;
import com.ey.dto.response.DoctorResponse;

public interface DoctorService {
	DoctorResponse registerDoctor(RegisterDoctorRequest request);

    DoctorResponse getDoctorById(Long doctorId);

    List<DoctorResponse> getDoctorsBySpecialization(String specialization);

    void deactivateDoctor(Long doctorId);

}
