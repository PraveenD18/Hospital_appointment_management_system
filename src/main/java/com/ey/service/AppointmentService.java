package com.ey.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.dto.request.AppointmentRequest;
import com.ey.dto.response.AppointmentResponse;
import com.ey.enums.AppointmentStatus;
import com.ey.enums.AppointmentType;

@Service
public interface AppointmentService {
	AppointmentResponse bookAppointment(AppointmentRequest request);

	void cancelAppointment(Long appointmentId);

	List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);

	List<AppointmentResponse> getAppointmentsByPatient(Long patientId);

	AppointmentResponse updateStatus(Long id, AppointmentStatus status);

	List<AppointmentResponse> getAllAppointments();

	List<AppointmentResponse> searchAppointments(LocalDateTime start, LocalDateTime end, Long doctorId, Long patientId,
			AppointmentStatus status, AppointmentType type);

	List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status);

}
