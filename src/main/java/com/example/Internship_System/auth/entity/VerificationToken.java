package com.example.Internship_System.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Used for OTP verification (register)
    @Column(length = 6)
    private String otp;

    // 🔹 Used for link-based verification (e.g., reset password)
    @Column(unique = true)
    private String token;

    @Column(nullable = false)
    private String purpose; // e.g. "REGISTER_OTP", "RESET_PASSWORD_LINK"

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    // ============================
    // 🔹 Constructors
    // ============================

    public VerificationToken() {}

    public VerificationToken(String otp, String purpose, LocalDateTime expiryDate, User user) {
        this.otp = otp;
        this.purpose = purpose;
        this.createdAt = LocalDateTime.now();
        this.expiryDate = expiryDate;
        this.user = user;
        this.used = false;
    }

    public VerificationToken(String token, String purpose, LocalDateTime expiryDate, User user, boolean isLinkBased) {
        this.token = token;
        this.purpose = purpose;
        this.createdAt = LocalDateTime.now();
        this.expiryDate = expiryDate;
        this.user = user;
        this.used = false;
    }

    // ============================
    // 🔹 Getters and Setters
    // ============================

    public Long getId() {
        return id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // ============================
    // 🔹 Helper method
    // ============================

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
