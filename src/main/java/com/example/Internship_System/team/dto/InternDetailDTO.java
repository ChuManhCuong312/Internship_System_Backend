package com.example.Internship_System.team.dto;

public class InternDetailDTO {
    private Integer internId;
    private String fullName;
    private String email;
    private String phone;
    private double gpa;
    private String major;
    private String school;

    public InternDetailDTO(Integer internId, String fullName, String email, String phone,
                           double gpa, String major, String school) {
        this.internId = internId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gpa = gpa;
        this.major = major;
        this.school = school;
    }

    // Getters & Setters
    public Integer getInternId() { return internId; }
    public void setInternId(Integer internId) { this.internId = internId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
}
