package com.example.Internship_System.auth.service;

import com.example.Internship_System.auth.dto.LoginRequest;
import com.example.Internship_System.auth.dto.RegisterRequest;
import com.example.Internship_System.auth.entity.Role;
import com.example.Internship_System.auth.entity.User;
import com.example.Internship_System.auth.entity.UserStatus;
import com.example.Internship_System.config.JwtUtils;
import com.example.Internship_System.repository.RoleRepository;
import com.example.Internship_System.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
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
        user.setStatus(UserStatus.ACTIVE);

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
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return null;
        }

        // ✅ Generate JWT token
        return jwtUtils.generateToken(user.getEmail());
    }
}
