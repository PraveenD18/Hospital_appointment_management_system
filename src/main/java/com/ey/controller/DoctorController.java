package com.ey.controller;

import com.ey.dto.request.RegisterDoctorRequest;
import com.ey.dto.response.DoctorResponse;
import com.ey.service.DoctorService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Register/create a doctor.
     * Access: ADMIN only
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DoctorResponse> registerDoctor(@RequestBody RegisterDoctorRequest request) {
        return ResponseEntity.ok(doctorService.registerDoctor(request));
    }

    /**
     * Get a doctor by id.
     * Access: ADMIN, or DOCTOR only if requesting their own profile.
     */
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR'))")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    /**
     * List all doctors.
     * Access: ADMIN only
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    /**
     * List doctors by specialization.
     * Access: ADMIN and DOCTOR (internal directory).
     * If you want this public for patients later, loosen this and return a safe DTO.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization));
    }

    /**
     * Deactivate/soft-delete a doctor.
     * Access: ADMIN only
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable Long id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
