package com.ey.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ey.dto.request.RegisterPatientRequest;
import com.ey.dto.response.PatientResponse;
import com.ey.enums.Role;
import com.ey.exception.ResourceNotFoundException;
import com.ey.mapper.PatientMapper;
import com.ey.model.Patient;
import com.ey.model.User;
import com.ey.repository.PatientRepository;
import com.ey.repository.UserRepository;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    public PatientServiceImpl(PatientRepository patientRepository,
                              UserRepository userRepository,
                              PatientMapper patientMapper,
                              PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.patientMapper = patientMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PatientResponse registerPatient(RegisterPatientRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PATIENT);
        userRepository.save(user);

        Patient patient = patientMapper.toEntity(request, user);
        patientRepository.save(patient);

        return patientMapper.toResponse(patient);
    }

    @Override
    public PatientResponse getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return patientMapper.toResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponse updatePatient(Long patientId, RegisterPatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patientMapper.updateEntity(patient, request);

        patientRepository.save(patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    public void deactivatePatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        patient.setActive(false);
        patientRepository.save(patient);
    }
}