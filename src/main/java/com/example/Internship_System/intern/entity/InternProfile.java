package com.example.Internship_System.intern.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "intern_users")
public class InternProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int intern_id;

    @Column(name = "user_id", nullable = false, unique = true)
    private int user_id;
    @Column(name = "school", nullable = false)
    private String school;
    @Column(name = "major", nullable = false)
    private String major;
    @Column(name = "dob", nullable = false)
    private LocalDate dob;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "cv_path", nullable = false)
    private String cv_path;
    @Column(name = "status")
    private String status;

    public int getIntern_id() {
        return intern_id;
    }

    public void setIntern_id(int intern_id) {
        this.intern_id = intern_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getCv_path() {
        return cv_path;
    }

    public void setCv_path(String cv_path) {
        this.cv_path = cv_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
