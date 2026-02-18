//package com.ey.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.ey.dto.request.LoginRequest;
//import com.ey.dto.response.LoginResponse;
//import com.ey.service.AuthService;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    // REGISTER
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody LoginRequest request) {
//        authService.register(request);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    // LOGIN
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//        return ResponseEntity.ok(authService.login(request));
//    }
//}
