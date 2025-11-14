package com.example.Internship_System.hr.dto;

public class HRInternDTO {
    private int internId;
    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String cvFile;
    private double gpa;
    private String permissionFile;
    private String status;
    private String major;
    private String school;

    public HRInternDTO() {}

    public HRInternDTO(int internId, int userId, String fullName, String email, String phone,
                       String cvFile, String internshipApplicationPath, double gpa,
                       String status, String major, String school) {
        this.internId = internId;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.cvFile = cvFile;
        this.gpa = gpa;
        this.permissionFile = internshipApplicationPath;
        this.status = status;
        this.major = major;
        this.school = school;
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

    public String getCvFile() { return cvFile; }
    public void setCvFile(String cvPath) { this.cvFile = cvFile; }

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
}
