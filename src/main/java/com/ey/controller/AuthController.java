package com.ey.controller;

import com.ey.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.email(), request.password());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}

record LoginRequest(String email, String password) {}
record LoginResponse(String token) {}