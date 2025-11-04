package com.example.Internship_System.auth.controller;

import com.example.Internship_System.auth.dto.LoginRequest;
import com.example.Internship_System.auth.dto.RegisterRequest;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.auth.entity.VerificationToken;
import com.example.Internship_System.auth.service.AuthService;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.repository.VerificationTokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          VerificationTokenRepository verificationTokenRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }
    // 🔹 Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String message = authService.register(request);
        // If registration succeeded, send verification email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        userOpt.ifPresent(authService::sendVerificationEmail);

        return ResponseEntity.ok(message + " Verification email sent.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            if (token == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password."));
            }

            return ResponseEntity.ok(Map.of("token", "Bearer " + token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid verification token");
        }

        VerificationToken verificationToken = optionalToken.get();
        if (verificationToken.isUsed()) {
            return ResponseEntity.badRequest().body("Token already used");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired");
        }

        User user = verificationToken.getUser();
        user.setStatus(UserStatus.PENDING_APPROVAL);
        userRepository.save(user);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return ResponseEntity.ok("Email verified successfully! Please wait for admin approval.");
    }
}
