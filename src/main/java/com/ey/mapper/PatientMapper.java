package com.ey.mapper;

import org.springframework.stereotype.Component;

import com.ey.dto.request.RegisterPatientRequest;
import com.ey.dto.response.PatientResponse;
import com.ey.enums.Gender;
import com.ey.model.Patient;
import com.ey.model.User;

@Component
public class PatientMapper {

    private Gender convertGender(String gender) {
        try {
            return Gender.valueOf(gender.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
    }

    public Patient toEntity(RegisterPatientRequest request, User user) {
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(convertGender(request.getGender()));
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
        patient.setActive(true);
        patient.setUser(user);
        return patient;
    }

    public void updateEntity(Patient patient, RegisterPatientRequest request) {
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(convertGender(request.getGender()));
        patient.setPhone(request.getPhone());
        patient.setAddress(request.getAddress());
    }

    public PatientResponse toResponse(Patient patient) {
        PatientResponse dto = new PatientResponse();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());
        dto.setGender(patient.getGender().name());
        dto.setPhone(patient.getPhone());
        dto.setAddress(patient.getAddress());
        dto.setActive(patient.isActive());
        return dto;
    }
}