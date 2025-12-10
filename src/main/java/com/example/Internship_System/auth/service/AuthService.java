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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
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

    // REGISTER
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

    // LOGIN (basic password verification)
    public Map<String, Object> login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();



        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return null;
        }

        // Generate JWT token with email + role
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().getName(),user.getUserId(), user.getFullName());

        // Return both token and user info
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getUserId());
        response.put("role", user.getRole().getName());
        response.put("email", user.getEmail());
        response.put("fullName:", user.getFullName());
        return response;
    }

    // Encode passowrd using BCrypt
    public String encodePassWord(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public void sendVerificationEmail(User user) {
        verificationTokenRepository.deleteAllByUserAndPurpose(user, "EMAIL_VERIFICATION_OTP");
        String otp = String.format("%06d", new Random().nextInt(999999)); // 6-digit OTP

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setOtp(otp);
        verificationToken.setPurpose("EMAIL_VERIFICATION_OTP");
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

        Optional<VerificationToken> lastTokenOpt =
                verificationTokenRepository.findByUserAndPurpose(user, "EMAIL_VERIFICATION_OTP");
        if (lastTokenOpt.isPresent()) {
            VerificationToken lastToken = lastTokenOpt.get();
            long secondsSinceLast = java.time.Duration.between(lastToken.getCreatedAt(), LocalDateTime.now()).getSeconds();
            if (secondsSinceLast < 60) {
                long waitSeconds = 60 - secondsSinceLast;
                return "WAIT_" + waitSeconds;  // <-- special format to tell frontend
            }
        }

        verificationTokenRepository.deleteAllByUserAndPurpose(user, "EMAIL_VERIFICATION_OTP");

        String otp = String.format("%06d", new Random().nextInt(999999));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setOtp(otp);
        verificationToken.setPurpose("EMAIL_VERIFICATION_OTP");
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

    // Send Reset Link
    @Transactional
    public String sendResetLink(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "User with this email does not exist.";
        }

        User user = userOpt.get();

        verificationTokenRepository.deleteAllByUserAndPurpose(user, "RESET_PASSWORD_LINK");

        // Generate reset token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token,
                "RESET_PASSWORD_LINK",
                LocalDateTime.now().plusMinutes(10),
                user,
                false
        );

        verificationTokenRepository.save(verificationToken);

        // Create link to frontend reset page
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;

        String subject = "🔐 Yêu cầu lấy lại mật khẩu đăng nhập System ";
        String body = "Xin chào " + user.getFullName() + ",\n\n"
                + "Chúng tôi đã nhận được yêu cầu lấy lại mật khẩu của bạn.\n"
                + "Hãy bấm vào link bên dưới để thiết lập mật khẩu mới (Liên kết chỉ có hiệu lực trong 10 phút từ thời điểm yêu cầu):\n\n"
                + resetUrl
                + "\n\nNếu đây không phải là yêu cầu của bạn, hãy bỏ qua email này.";

        emailService.sendEmail(user.getEmail(), subject, body);
        return "Link thiết lập mẩu khẩu mới đã được gửi thành công.";
    }

    // Validate token before reset
    public boolean validateResetToken(String token) {
        Optional<VerificationToken> tokenOpt =
                verificationTokenRepository.findByTokenAndPurpose(token, "RESET_PASSWORD_LINK");

        if (tokenOpt.isEmpty()) return false;

        VerificationToken vt = tokenOpt.get();
        return !vt.isUsed() && !vt.isExpired();
    }

    // Perform reset password
    @Transactional
    public String resetPassword(String token, String newPassword) {
        Optional<VerificationToken> tokenOpt =
                verificationTokenRepository.findByTokenAndPurpose(token, "RESET_PASSWORD_LINK");

        if (tokenOpt.isEmpty()) {
            return "Invalid or expired token.";
        }

        VerificationToken vt = tokenOpt.get();
        if (vt.isUsed() || vt.isExpired()) {
            return "Invalid or expired token.";
        }

        User user = vt.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        vt.setUsed(true);
        verificationTokenRepository.save(vt);

        return "Cập nhật mật khẩu thành công!";
    }

    @Transactional
    public String resendResetLink(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "Email not found!";
        }

        User user = optionalUser.get();

        // Check if an active (unused and unexpired) reset link already exists
        Optional<VerificationToken> lastTokenOpt = verificationTokenRepository
                .findByUserAndPurpose(user, "RESET_PASSWORD_LINK");

        if (lastTokenOpt.isPresent()) {
            VerificationToken lastToken = lastTokenOpt.get();
            long secondsSinceLast = java.time.Duration.between(lastToken.getCreatedAt(), LocalDateTime.now()).getSeconds();
            if (!lastToken.isUsed() && !lastToken.isExpired() && secondsSinceLast < 60) {
                long waitSeconds = 60 - secondsSinceLast;
                return "WAIT_" + waitSeconds;  // frontend can show countdown
            }
        }

        // Delete old reset tokens
        verificationTokenRepository.deleteAllByUserAndPurpose(user, "RESET_PASSWORD_LINK");

        // Generate new reset token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token,
                "RESET_PASSWORD_LINK",
                LocalDateTime.now().plusMinutes(10),
                user,
                false
        );

        verificationTokenRepository.save(verificationToken);

        // Send email
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;
        String subject = "🔐 Yêu cầu lấy lại mật khẩu đăng nhập System";
        String body = "Xin chào " + user.getFullName() + ",\n\n"
                + "Hãy bấm vào link bên dưới để thiết lập mật khẩu mới (Liên kết chỉ có hiệu lực trong 10 phút):\n\n"
                + resetUrl
                + "\n\nNếu đây không phải là yêu cầu của bạn, hãy bỏ qua email này.";

        emailService.sendEmail(user.getEmail(), subject, body);

        return "Link thiết lập mật khẩu mới đã được gửi thành công!";
    }

}
