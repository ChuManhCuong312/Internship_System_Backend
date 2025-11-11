package com.example.Internship_System.auth.controller;

import com.example.Internship_System.auth.dto.LoginRequest;
import com.example.Internship_System.auth.dto.RegisterRequest;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.auth.entity.VerificationToken;
import com.example.Internship_System.auth.service.AuthService;
import com.example.Internship_System.config.JwtUtils;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.repository.VerificationTokenRepository;
import com.example.Internship_System.utils.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          VerificationTokenRepository verificationTokenRepository,
                          EmailService emailService,
                          JwtUtils jwtUtils) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
        this.jwtUtils =jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String message = authService.register(request);
        // If registration succeeded, send verification email
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        userOpt.ifPresent(authService::sendVerificationEmail);

        return ResponseEntity.ok(message + " Verification email sent.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            String token = authService.login(request);
            if (token == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password."));
            }

            // Set JWT as HTTP-only cookie
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true) // use HTTPS in production
                    .path("/")
                    .maxAge(30 * 60) // 30 minutes
                    .sameSite("Strict")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return ResponseEntity.ok(Map.of("message", "Login successful"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(@CookieValue(name = "token", required = false) String token) {
        if (token == null || !jwtUtils.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String email = jwtUtils.extractEmail(token);
        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        var user = userOpt.get();
        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "role", user.getRole().getName()
        ));
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<VerificationToken> optionalToken =
                verificationTokenRepository.findByUser_EmailAndOtp(email, otp);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        VerificationToken verificationToken = optionalToken.get();

        if (verificationToken.isUsed()) {
            return ResponseEntity.badRequest().body("OTP already used");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("OTP expired");
        }

        User user = verificationToken.getUser();
        user.setStatus(UserStatus.PENDING_APPROVAL);
        userRepository.save(user);

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return ResponseEntity.ok("OTP verified successfully! Please wait for admin approval.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        String message = authService.resendOtp(email);

        // Handle rate-limit and other error messages gracefully
        if (message.startsWith("Vui lòng") || message.contains("Email not found") || message.contains("User already")) {
            return ResponseEntity.badRequest().body(message);
        }

        return ResponseEntity.ok(message);
    }

    @GetMapping("/oauth-success")
    public ResponseEntity<String> oauthSuccess() {
        return ResponseEntity.ok("OAuth login successful! Please wait for admin approval.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String msg = authService.sendResetLink(email);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<Boolean> validateResetToken(@RequestParam String token) {
        boolean valid = authService.validateResetToken(token);
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String msg = authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/resend-reset-link")
    public ResponseEntity<String> resendResetLink(@RequestParam String email) {
        String result = authService.resendResetLink(email);
        return ResponseEntity.ok(result);
    }
}
