package com.example.Internship_System.auth.service;

import com.example.Internship_System.auth.dto.LoginRequest;
import com.example.Internship_System.auth.dto.RegisterRequest;
import com.example.Internship_System.auth.entity.Role;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.auth.entity.VerificationToken;
import com.example.Internship_System.config.JwtUtils;
import com.example.Internship_System.repository.RoleRepository;
import com.example.Internship_System.repository.UserRepository;
import com.example.Internship_System.repository.VerificationTokenRepository;
import com.example.Internship_System.utils.EmailService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    // inject repositories + emailService
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils,VerificationTokenRepository verificationTokenRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    // 🔹 REGISTER
    public String register(RegisterRequest request) {
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already in use!";
        }

        // Create user entity
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.INACTIVE);

        // Assign default role = "INTERN"
        Optional<Role> internRole = roleRepository.findByName("INTERN");
        internRole.ifPresent(user::setRole);

        // Save
        userRepository.save(user);
        return "Registration successful. Awaiting admin approval.";
    }

    // 🔹 LOGIN (basic password verification)
    public String login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();

        // Check if user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Account not active. Please wait for admin approval.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return null;
        }

        // ✅ Generate JWT token with email + role
        return jwtUtils.generateToken(user.getEmail(), user.getRole().getName());
    }

    // Encode passowrd using BCrypt
    public String encodePassWord(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        verificationTokenRepository.save(verificationToken);

        String verifyUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        String subject = "Xác thực tài khoản Internship System";
        String body = "Chào " + user.getFullName() + ",\n\n" +
                "Vui lòng xác thực tài khoản bằng cách click vào link sau: \n" + verifyUrl +
                "\n\nLiên kết này sẽ hết hạn sau 10 phút.";

        emailService.sendEmail(user.getEmail(), subject, body);
    }
}
