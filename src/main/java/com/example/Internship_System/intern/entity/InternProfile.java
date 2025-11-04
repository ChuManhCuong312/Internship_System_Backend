package com.example.Internship_System.intern.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "intern_users")
public class InternProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intern_id", nullable = false)
    private int internId;

    @Column(name = "user_id", nullable = false, unique = true)
    private int userId;
    @Column(name = "school", nullable = false)
    private String school;
    @Column(name = "major", nullable = false)
    private String major;
    @Column(name = "dob", nullable = false)
    private LocalDate dob;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "cv_path", nullable = false)
    private String cvPath;
    @Column(name = "status")
    private String status;

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public int getUserId() {
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

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
