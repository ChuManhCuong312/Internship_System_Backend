package com.example.Internship_System.repository;

import com.example.Internship_System.auth.entity.VerificationToken;
import com.example.Internship_System.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUser(User user);
    Optional<VerificationToken> findByUser_EmailAndOtp(String email, String otp);
    void deleteAllByUser(User user);
}
