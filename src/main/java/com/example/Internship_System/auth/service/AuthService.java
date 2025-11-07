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
import org.springframework.transaction.annotation.Transactional; // ✅ Import this

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
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
        user.setPhone(request.getPhone());
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
        verificationTokenRepository.deleteAllByUser(user);
        String otp = String.format("%06d", new Random().nextInt(999999)); // 6-digit OTP

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setOtp(otp);
        verificationToken.setUser(user);
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        verificationToken.setUsed(false);
        verificationTokenRepository.save(verificationToken);

        String subject = "Mã xác thực tài khoản Internship System";
        String body = "Chào " + user.getFullName() + ",\n\n" +
                "Mã OTP của bạn là: " + otp +
                "\n\nMã này sẽ hết hạn sau 10 phút.";

        emailService.sendEmail(user.getEmail(), subject, body);
    }

    @Transactional
    public String resendOtp(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Email not found!";
        }

        User user = optionalUser.get();

        if (user.getStatus() != UserStatus.INACTIVE) {
            return "User already verified or pending approval.";
        }

        Optional<VerificationToken> lastTokenOpt = verificationTokenRepository.findByUser(user);
        if (lastTokenOpt.isPresent()) {
            VerificationToken lastToken = lastTokenOpt.get();
            long secondsSinceLast = java.time.Duration.between(lastToken.getCreatedAt(), LocalDateTime.now()).getSeconds();
            if (secondsSinceLast < 60) {
                long waitSeconds = 60 - secondsSinceLast;
                return "WAIT_" + waitSeconds;  // <-- special format to tell frontend
            }
        }

        verificationTokenRepository.deleteAllByUser(user);

        String otp = String.format("%06d", new Random().nextInt(999999));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setOtp(otp);
        verificationToken.setUsed(false);
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        verificationTokenRepository.save(verificationToken);

        String subject = "Mã OTP xác thực tài khoản Internship System (Gửi lại)";
        String body = "Chào " + user.getFullName() + ",\n\n"
                + "Đây là mã OTP mới của bạn: " + otp
                + "\n\nMã sẽ hết hạn sau 10 phút.\n\n"
                + "Nếu bạn không yêu cầu gửi lại OTP, vui lòng bỏ qua email này.";

        emailService.sendEmail(user.getEmail(), subject, body);

        return "OTP mới đã được gửi đến email của bạn!";
    }


}
