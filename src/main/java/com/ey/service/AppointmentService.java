package com.ey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.dto.request.AppointmentRequest;
import com.ey.dto.response.AppointmentResponse;
import com.ey.enums.AppointmentStatus;

@Service
public interface AppointmentService {
	AppointmentResponse bookAppointment(AppointmentRequest request);

    void cancelAppointment(Long appointmentId);

    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);

    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);

	AppointmentResponse updateStatus(Long id, AppointmentStatus status);

	List<AppointmentResponse> getAllAppointments();

}
