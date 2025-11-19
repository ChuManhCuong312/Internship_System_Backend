package com.example.Internship_System.intern.entity;

import com.example.Internship_System.validation.MinimumAge;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "intern_users")
public class InternProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intern_id", nullable = false)
    private int internId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @NotBlank(message = "Trường học là bắt buộc")
    @Size(min = 2, max = 150, message = "Tên trường phải từ 2 đến 150 ký tự")
    @Column(name = "school")
    private String school;


    @NotBlank(message = "Ngành học là bắt buộc")
    @Size(min = 2, max = 150, message = "Tên ngành phải từ 2 đến 150 ký tự")
    @Column(name = "major")
    private String major;

    @NotNull(message = "Ngày sinh là bắt buộc")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @MinimumAge(value = 18, message = "Tuổi phải từ 18 trở lên")
    @Column(name = "dob")
    private LocalDate dob;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    @Size(min = 5, max = 255, message = "Địa chỉ phải từ 5 đến 255 ký tự")
    @Column(name = "address")
    private String address;

    @Size(max = 255, message = "Tên file CV không được vượt quá 255 ký tự")
    @Column(name = "cv_path")
    private String cvPath;

    @Size(max = 255, message = "Must not exceed 255 characters")
    @Column(name = "internship_application_path")
    private String permissionFile;

    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|NO_FILE)?$",
            message = "Trạng thái phải là một trong: PENDING, APPROVED, REJECTED, NO_FILE")
    @Column(name = "status")
    private String status;

    @Pattern(regexp = "^(MALE|FEMALE)?$",
            message = "Giới tính phải là MALE hoặc FEMALE")
    @Column(name = "gender")
    private String gender;

    @Size(max = 255, message = "Đường dẫn ảnh đại diện không được vượt quá 255 ký tự")
    @Column(name = "intern_image_path")
    private String avatar;

    @Positive(message = "GPA phải lớn hơn 0")
    @Column(name = "gpa", nullable = false)
    private double gpa;

    @Column(name = "rejection_reason")
    @Size(max = 255)
    private String rejectionReason;

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCvFile() {
        return cvPath;
    }

    public void setCvFile(String cvFile) {
        this.cvPath = cvFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getPermissionFile() {
        return permissionFile;
    }

    public void setPermissionFile(String permissionFile) {
        this.permissionFile = permissionFile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
