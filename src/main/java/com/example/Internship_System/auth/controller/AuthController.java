package com.example.Internship_System.auth.controller;

import com.example.Internship_System.auth.dto.LoginRequest;
import com.example.Internship_System.auth.dto.RegisterRequest;
import com.example.Internship_System.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // 🔹 Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String message = authService.register(request);
        return ResponseEntity.ok(message);
    }

    // 🔹 Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        if (token == null) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
        return ResponseEntity.ok("Bearer " + token);
    }
}
