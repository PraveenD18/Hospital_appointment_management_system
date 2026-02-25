package com.ey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ey.dto.request.RegisterPatientRequest;
import com.ey.dto.response.PatientResponse;
import com.ey.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Register/create a patient.
     * Access: ADMIN only (If you need public self-signup, change to permitAll or separate endpoint)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody RegisterPatientRequest request) {
        return ResponseEntity.ok(patientService.registerPatient(request));
    }

    /**
     * Get a patient by id.
     * Access: ADMIN, or PATIENT only if requesting their own profile.
     */
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.patientId == #id)")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    /**
     * List all patients.
     * Access: ADMIN only
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * Update patient profile.
     * Access: ADMIN, or PATIENT only for self.
     */
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PATIENT') and principal.patientId == #id)")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @RequestBody RegisterPatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    /**
     * Deactivate/soft-delete a patient.
     * Access: ADMIN only
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivatePatient(@PathVariable Long id) {
        patientService.deactivatePatient(id);
        return ResponseEntity.noContent().build();
    }
}