package com.ey.mapper;

import org.springframework.stereotype.Component;

import com.ey.dto.request.AppointmentRequest;
import com.ey.dto.response.AppointmentResponse;
import com.ey.enums.AppointmentStatus;
import com.ey.model.Appointment;
import com.ey.model.Doctor;
import com.ey.model.Patient;

@Component
public class AppointmentMapper {
	public Appointment toEntity(AppointmentRequest request, Doctor doctor, Patient patient) {
		Appointment appointment = new Appointment();
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		appointment.setAppointmentDateTime(request.getAppointmentDateTime());
		appointment.setStatus(AppointmentStatus.BOOKED);
		return appointment;
	}

	public AppointmentResponse toResponse(Appointment appointment) {
		AppointmentResponse dto = new AppointmentResponse();
		dto.setAppointmentId(appointment.getAppointmentId());
		dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
		dto.setStatus(appointment.getStatus());
		Doctor doc = appointment.getDoctor();
		Patient pat = appointment.getPatient();
		dto.setDoctorName(doc != null ? doc.getName() : null);
		dto.setPatientName(pat != null ? pat.getName() : null);

		return dto;
	}

	public void updateStatus(Appointment appointment, AppointmentStatus newStatus) {
		appointment.setStatus(newStatus);
	}

}