package com.ey.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam("value") AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctor(doctorId)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatient(patientId)
        );
    }

    // --- Search & Filter ---

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

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponse>> getByStatus(
            @PathVariable AppointmentStatus status
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }
}
