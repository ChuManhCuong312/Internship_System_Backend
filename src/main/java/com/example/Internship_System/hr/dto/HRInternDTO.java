package com.example.Internship_System.hr.dto;

import java.time.LocalDate;

public class HRInternDTO {
    private int internId;
    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String cvPath;
    private double gpa;
    private String permissionFile;
    private String status;
    private String major;
    private String school;
    private LocalDate dob;
    private String address;
    private String universityConfirm;

    public HRInternDTO() {}

    public HRInternDTO(int internId, int userId, String fullName, String email, String phone,
                       String cvPath, String permissionFile, double gpa,
                       String status, String major, String school,
                       LocalDate dob, String address, String gender,
                       String universityConfirm) {
        this.internId = internId;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.cvPath = cvPath;
        this.permissionFile = permissionFile;
        this.gpa = gpa;
        this.status = status;
        this.major = major;
        this.school = school;
        this.dob = dob;
        this.address = address;
        this.universityConfirm = universityConfirm;
    }

    // Getters & Setters
    public int getInternId() { return internId; }
    public void setInternId(int internId) { this.internId = internId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCvPath() { return cvPath; }
    public void setCvPath(String cvPath) { this.cvPath = cvPath; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getPermissionFile() { return permissionFile; }
    public void setPermissionFile(String permissionFile) { this.permissionFile = permissionFile; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getUniversityConfirm() { return universityConfirm; }
    public void setUniversityConfirm(String universityConfirm) { this.universityConfirm = universityConfirm; }
}
