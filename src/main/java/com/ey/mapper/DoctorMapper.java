package com.ey.mapper;

import com.ey.dto.response.DoctorResponse;
import com.ey.model.Doctor;

public class DoctorMapper {
	public static DoctorResponse toResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setLocation(doctor.getLocation());
        dto.setPhone(doctor.getPhone());
        dto.setAvailableFrom(doctor.getAvailableFrom());
        dto.setAvailableTo(doctor.getAvailableTo());
        dto.setActive(doctor.isActive());
        return dto;
	}
}
