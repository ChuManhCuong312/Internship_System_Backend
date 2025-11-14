package com.example.Internship_System.admin.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private int userId;
    private String email;
    private String fullName;
    private String phone;
    private String status;
    private LocalDateTime createdAt;
    private int roleId;
    private String password; // thêm trường này để nhận mật khẩu khi tạo/sửa

    public UserDTO(int userId, String email, String fullName, String phone,
                   String status, LocalDateTime createdAt, int roleId, String password) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.roleId = roleId;
        this.password = password;
    }

    // ✅ Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
