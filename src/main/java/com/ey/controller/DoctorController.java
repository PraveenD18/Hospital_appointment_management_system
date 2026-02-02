package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.RegisterDoctorRequest;
import com.ey.dto.response.DoctorResponse;
import com.ey.service.DoctorService;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

	private final DoctorService doctorService;

	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@PostMapping
	public ResponseEntity<DoctorResponse> registerDoctor(@RequestBody RegisterDoctorRequest request) {
		return ResponseEntity.ok(doctorService.registerDoctor(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DoctorResponse> getDoctor(@PathVariable Long id) {
		return ResponseEntity.ok(doctorService.getDoctorById(id));
	}

	@GetMapping("/specialization/{specialization}")
	public ResponseEntity<List<DoctorResponse>> getBySpecialization(@PathVariable String specialization) {
		return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deactivateDoctor(@PathVariable Long id) {
		doctorService.deactivateDoctor(id);
		return ResponseEntity.noContent().build();
	}
}