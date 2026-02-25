package com.ey.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ey.dto.request.AppointmentRequest;
import com.ey.dto.request.AppointmentSearchRequest;
import com.ey.dto.response.AppointmentResponse;
import com.ey.enums.AppointmentStatus;
import com.ey.enums.AppointmentType;
import com.ey.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Book an appointment.
     * Access: ADMIN (on behalf) and PATIENT (self booking).
     * If you want doctors to create bookings (e.g., front desk flow), add DOCTOR to the roles list.
     */
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody AppointmentRequest request) {
        // Service should enforce: PATIENT can only book for self (request.patientId == currentPatientId)
        return ResponseEntity.ok(appointmentService.bookAppointment(request));
    }

    /**
     * Update appointment status (SCHEDULED/COMPLETED/CANCELLED/NO_SHOW).
     * Access: ADMIN, or DOCTOR (only for appointments assigned to them) -> enforce in service.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam("value") AppointmentStatus status) {
        // Service should enforce: if role=DOCTOR, appointment.doctorId == currentDoctorId
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    /**
     * Get all appointments (global).
     * Access: ADMIN only.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    /**
     * Get appointments for a doctor.
     * Access: ADMIN, or DOCTOR (only their own doctorId) -> enforce in service.
     */
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(@PathVariable Long doctorId) {
        // Service should enforce: if role=DOCTOR, doctorId == currentDoctorId
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    /**
     * Get appointments for a patient.
     * Access: ADMIN, or PATIENT (only their own patientId) -> enforce in service.
     */
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(@PathVariable Long patientId) {
        // Service should enforce: if role=PATIENT, patientId == currentPatientId
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // --- Search & Filter ---

    /**
     * Search appointments (query params).
     * Access: ADMIN only.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<AppointmentResponse>> searchAppointments(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) AppointmentType type
    ) {
        return ResponseEntity.ok(
                appointmentService.searchAppointments(start, end, doctorId, patientId, status, type)
        );
    }

    /**
     * Search appointments (POST body).
     * Access: ADMIN only.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<List<AppointmentResponse>> searchAppointmentsBody(
            @RequestBody AppointmentSearchRequest request
    ) {
        return ResponseEntity.ok(
                appointmentService.searchAppointments(
                        request.getStart(),
                        request.getEnd(),
                        request.getDoctorId(),
                        request.getPatientId(),
                        request.getStatus(),
                        request.getType()
                )
        );
    }

    /**
     * Get appointments by status.
     * Access: ADMIN only.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponse>> getByStatus(@PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }
}