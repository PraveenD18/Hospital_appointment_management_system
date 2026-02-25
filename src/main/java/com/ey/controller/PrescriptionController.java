package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ey.dto.request.PrescriptionRequest;
import com.ey.dto.request.PrescriptionUpdateRequest;
import com.ey.dto.response.PrescriptionResponse;
import com.ey.enums.PrescriptionStatus;
import com.ey.service.PrescriptionService;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Create a prescription.
     * Access: ADMIN and DOCTOR.
     * Service MUST enforce: if role=DOCTOR, doctor must be assigned to the appointment/patient.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(@RequestBody PrescriptionRequest request) {
        // Service ownership checks: currentDoctorId must match appointment.doctorId (if DOCTOR)
        return ResponseEntity.ok(prescriptionService.createPrescription(request));
    }

    /**
     * Get prescription by id.
     * Access: ADMIN, DOCTOR (assigned), PATIENT (owner).
     * Service MUST enforce: 
     *  - DOCTOR -> prescription.appointment.doctorId == currentDoctorId
     *  - PATIENT -> prescription.patientId == currentPatientId
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getById(id));
    }

    /**
     * Get prescription by appointment id (1:1 or latest depending on your service).
     * Access: ADMIN, DOCTOR (assigned to that appointment), PATIENT (owner of that appointment).
     * Service MUST enforce ownership as above.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionResponse> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionByAppointment(appointmentId));
    }

    /**
     * List prescriptions by status.
     * Access: ADMIN only (reporting/global query).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PrescriptionResponse>> getByStatus(@PathVariable PrescriptionStatus status) {
        return ResponseEntity.ok(prescriptionService.getByStatus(status));
    }

    /**
     * List all prescriptions (global).
     * Access: ADMIN only.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getAll() {
        return ResponseEntity.ok(prescriptionService.getAll());
    }

    /**
     * Update prescription content.
     * Access: ADMIN, DOCTOR (assigned).
     * Service MUST enforce: if role=DOCTOR, doctor must be the prescriber / assigned to that appointment.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> update(
            @PathVariable Long id,
            @RequestBody PrescriptionUpdateRequest request) {
        return ResponseEntity.ok(prescriptionService.update(id, request));
    }

    /**
     * Update prescription status (e.g., ISSUED, CANCELLED, etc).
     * Access: ADMIN, DOCTOR (assigned).
     * Service MUST enforce ownership for doctors.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<PrescriptionResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam("value") PrescriptionStatus status) {
        return ResponseEntity.ok(prescriptionService.updateStatus(id, status));
    }

    /**
     * Delete prescription (via PUT, per your existing design).
     * Access: ADMIN only.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/delete")
    public ResponseEntity<Void> deleteViaPut(@PathVariable Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}