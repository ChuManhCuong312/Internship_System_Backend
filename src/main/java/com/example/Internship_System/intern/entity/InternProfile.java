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

    @Column(name = "school", nullable = false)
    @NotBlank(message = "Trường học là bắt buộc")
    @Size(min = 2, max = 150, message = "Tên trường của bạn phải từ 2 đến 150 ký tự")
    private String school;

    @Column(name = "major", nullable = false)
    @NotBlank(message = "Ngành học là bắt buộc")
    @Size(min = 2, max = 150, message = "Tên ngành phải từ 2 đến 150 ký tự")
    private String major;

    @Column(name = "dob", nullable = false)
    @NotNull(message = "Ngày sinh là bắt buộc")
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @MinimumAge(value = 18, message = "Tuổi phải từ 18 trở lên")
    private LocalDate dob;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Địa chỉ là bắt buộc")
    @Size(min = 5, max = 255, message = "Địa chỉ phải từ 5 đến 255 ký tự")
    private String address;

    @Column(name = "cv_path")
    @Size(max = 255, message = "Đường dẫn file phải ngắn hơn 255 ký tự")
    private String cvPath;

    @Column(name = "internship_application_path")
    @Size(max = 255, message = "Đường dẫn file phải ngắn hơn 255 ký tự")
    private String permissionFile;

    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|NO_FILE)?$",
            message = "Trạng thái phải hợp lệ: PENDING, APPROVED, REJECTED, NO_FILE")
    @Column(name = "status")
    private String status;

    @Pattern(regexp = "^(MALE|FEMALE)?$",
            message = "Chọn 1 giới tính")
    @Column(name = "gender")
    private String gender;

    @Size(max = 255, message = "Đường dẫn file phải ngắn hơn 255 kí tự, định dạng .png hoặc .jpg")
    @Column(name = "intern_image_path")
    private String avatar;

    @Column(name = "gpa", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "GPA phải lớn hơn 0")
    @DecimalMax(value = "4.0", inclusive = true, message = "GPA phải nhỏ hơn hoặc bằng 4.0")
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

    public void setCvFile(String cvPath) {
        this.cvPath = cvPath;
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
