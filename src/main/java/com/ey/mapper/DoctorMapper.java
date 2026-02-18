package com.ey.mapper;

import org.springframework.stereotype.Component;
import com.ey.dto.request.RegisterDoctorRequest;
import com.ey.dto.response.DoctorResponse;
import com.ey.enums.City;
import com.ey.enums.Specialization;
import com.ey.model.Doctor;
import com.ey.model.User;

import java.time.LocalTime;

@Component
public class DoctorMapper {

    public Doctor toEntity(RegisterDoctorRequest request, User user) {
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSpecialization(
                Specialization.valueOf(request.getSpecialization().trim().toUpperCase())
        );
        doctor.setLocation(
                City.valueOf(request.getLocation().trim().toUpperCase())
        );
        doctor.setPhone(request.getPhone());
        doctor.setAvailableFrom(LocalTime.parse(request.getAvailableFrom()));
        doctor.setAvailableTo(LocalTime.parse(request.getAvailableTo()));
        doctor.setActive(true);
        doctor.setUser(user);

        return doctor;
    }

    public DoctorResponse toResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization().name());
        dto.setLocation(doctor.getLocation().name());
        dto.setPhone(doctor.getPhone());
        dto.setAvailableFrom(doctor.getAvailableFrom().toString());
        dto.setAvailableTo(doctor.getAvailableTo().toString());
        dto.setActive(doctor.isActive());

        return dto;
    }
}