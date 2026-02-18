package com.ey.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ey.dto.request.RegisterDoctorRequest;
import com.ey.dto.response.DoctorResponse;
import com.ey.enums.Role;
import com.ey.enums.Specialization;
import com.ey.exception.ResourceNotFoundException;
import com.ey.mapper.DoctorMapper;
import com.ey.model.Doctor;
import com.ey.model.User;
import com.ey.repository.DoctorRepository;
import com.ey.repository.UserRepository;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             UserRepository userRepository,
                             DoctorMapper doctorMapper,
                             PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.doctorMapper = doctorMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public DoctorResponse registerDoctor(RegisterDoctorRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DOCTOR);
        userRepository.save(user);

        Doctor doctor = doctorMapper.toEntity(request, user);
        doctorRepository.save(doctor);

        return doctorMapper.toResponse(doctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        Specialization specEnum = Specialization.valueOf(specialization.trim().toUpperCase());
        return doctorRepository.findBySpecialization(specEnum)
                .stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }
}