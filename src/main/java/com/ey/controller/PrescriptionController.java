package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    @PostMapping
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @RequestBody PrescriptionRequest request) {
        return ResponseEntity.ok(prescriptionService.createPrescription(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getById(id));
    }
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionResponse> getByAppointment(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionByAppointment(appointmentId));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PrescriptionResponse>> getByStatus(
            @PathVariable PrescriptionStatus status) {
        return ResponseEntity.ok(prescriptionService.getByStatus(status));
    }
    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getAll() {
        return ResponseEntity.ok(prescriptionService.getAll());
    }
    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponse> update(
            @PathVariable Long id,
            @RequestBody PrescriptionUpdateRequest request) {
        return ResponseEntity.ok(prescriptionService.update(id, request));
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<PrescriptionResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam("value") PrescriptionStatus status) {
        return ResponseEntity.ok(prescriptionService.updateStatus(id, status));
    }
    @PutMapping("/{id}/delete")
    public ResponseEntity<Void> deleteViaPut(@PathVariable Long id) {
        prescriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}